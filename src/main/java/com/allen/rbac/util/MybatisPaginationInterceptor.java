package com.allen.rbac.util;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 分页拦截器,附加了返回行数限制功能 ，查询不带limit或者limit>{maxrow}时，自动设置limit {maxrow}
 *
 * @author Ternence
 * @date 2016年12月14日
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisPaginationInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisPaginationInterceptor.class);

    Dialect dialect = new MySql5Dialect();

    // 最大查询条数为1000。可通过配置动态调整
    private int rowLimit = 1000;

    private BoundSql newBoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject, BoundSql oldBoundSql) {
        BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);

        //将创建的newBoundSql相比较从MappedStatement中取得的boundSql丢失了additionalParameters补充回去
        for (ParameterMapping mapping : oldBoundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (oldBoundSql.hasAdditionalParameter(prop)) {
                boundSql.setAdditionalParameter(prop, oldBoundSql.getAdditionalParameter(prop));
            }
        }
        return boundSql;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        String sqlId = mappedStatement.getId();

        Object parameter = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        if(null == boundSql) {
            LOGGER.warn("BoundSql is null");
            return null;
        }

        String originalSql = boundSql.getSql().trim();
        if (originalSql == null || originalSql.isEmpty()) {
            LOGGER.warn("sql is empty : sql = " + originalSql);
            return null;
        }

        Object parameterObject = boundSql.getParameterObject();

        RowBounds rowBounds = (RowBounds) args[2];

        // 分页参数--上下文传参
        PageInfo pageInfo = null;
        // map传参每次都将currentPage重置,先判读map再判断context
        if (parameterObject instanceof PageInfo) {
            pageInfo = (PageInfo) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) parameterObject;
            if (map.containsKey("page")) {
                pageInfo = (PageInfo) map.get("page");
            }
        } else if (null != parameterObject) {
            pageInfo = (PageInfo) ReflectionUtil.getValueByFieldName(parameterObject, "page");
        }

        // 后面用到了context的东东
        if (pageInfo != null && pageInfo.isPagination() == true) {
            if (pageInfo.getPageSize() > rowLimit) {
                LOGGER.warn("PageSize大于" + rowLimit);
                pageInfo.setPageSize(rowLimit);
            }

            int totalRows = pageInfo.getTotalRows();
            // 得到总记录数
            if (totalRows == 0 && pageInfo.isNeedCount()) {
                StringBuffer countSql = new StringBuffer();
                countSql.append(MySql5PageHepler.getCountString(originalSql));
                Connection connection = null;
                PreparedStatement countStmt = null;
                try {
                    connection = mappedStatement.getConfiguration().getEnvironment().getDataSource()
                            .getConnection();
                    countStmt = connection.prepareStatement(countSql.toString());

                    BoundSql countBS =
                            newBoundSql(mappedStatement.getConfiguration(), countSql.toString(), boundSql.getParameterMappings(), parameterObject, boundSql);


                    Field metaParamsField = ReflectionUtil
                            .getFieldByFieldName(boundSql, "metaParameters");
                    if (metaParamsField != null) {
                        MetaObject mo = (MetaObject) ReflectionUtil
                                .getValueByFieldName(boundSql, "metaParameters");
                        ReflectionUtil.setValueByFieldName(countBS, "metaParameters", mo);
                    }
                    setParameters(countStmt, mappedStatement, countBS, parameterObject);
                    ResultSet rs = countStmt.executeQuery();
                    if (rs.next()) {
                        totalRows = rs.getInt(1);
                    }
                    rs.close();
                } finally {
                    if (countStmt != null) {
                        countStmt.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                }

            }

            // 分页计算
            pageInfo.init(totalRows, pageInfo.getPageSize(), pageInfo.getCurrentPage());

            if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
                rowBounds = new RowBounds(pageInfo.getPageSize() * (pageInfo.getCurrentPage() - 1), pageInfo.getPageSize());

            }

            // 分页查询 本地化对象 修改数据库注意修改实现
            String pagesql = dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit());
            invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            BoundSql newBoundSql =
                    newBoundSql(mappedStatement.getConfiguration(), pagesql, boundSql.getParameterMappings(), boundSql.getParameterObject(), boundSql);

            Field metaParamsField = ReflectionUtil.getFieldByFieldName(boundSql, "metaParameters");
            if (metaParamsField != null) {
                MetaObject mo = (MetaObject) ReflectionUtil.getValueByFieldName(boundSql, "metaParameters");
                ReflectionUtil.setValueByFieldName(newBoundSql, "metaParameters", mo);
            }
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

            invocation.getArgs()[0] = newMs;
        }

        return invocation.proceed();

    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties arg0) {

    }

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement "
                                + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(buildKeyProperty(ms.getKeyProperties()));
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.useCache(ms.isUseCache());
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }

    private static String buildKeyProperty(String[] props) {
        if (null != props && props.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (String p : props) {
                sb.append(p).append(",");
            }

            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

}
