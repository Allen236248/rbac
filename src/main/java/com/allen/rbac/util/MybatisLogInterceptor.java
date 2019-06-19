package com.allen.rbac.util;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * Mybatis SQL 日志拦截器
 * <p>
 * 1、打印SQL
 * 2、打印慢SQL
 * 3、打印导致Too Many Rows的SQL
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisLogInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisLogInterceptor.class);

    private static boolean printSQL = true;

    // TODO 暂时写死最大查询条数为1000 @Value("${sql.rows.max.return}")
    private Integer tooManyRows = 1000;

    public Object intercept(Invocation invocation) throws Throwable {
        Object parameter = null;
        Object[] args = invocation.getArgs();
        if (args.length > 1) {
            parameter = args[1];
        }
        MappedStatement mappedStatement = (MappedStatement) args[0];
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();

        String sql = buildSqlLog(configuration, boundSql, sqlId);

        Object returnValue = null;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
        } catch (Exception e) {
            LOGGER.error("SQL执行失败。{}", sql);
            throw e;
        }
        long end = System.currentTimeMillis();
        long time = (end - start);

        if (isPrintSQL()) {
            LOGGER.info(sql);
        }
        if (time > 2000) {
            LOGGER.warn("[SLOW SQL] 发现慢SQL，{}，耗时:{}毫秒", sql, time);
        }
        if (returnValue != null && returnValue instanceof List && ((List<Object>) returnValue).size() > tooManyRows) {
            LOGGER.warn("[TOO MANY ROWS] {} 返回数据行数{}大于{}", sql, ((List<Object>) returnValue).size(), tooManyRows);
        }
        return returnValue;
    }

    public static String buildSqlLog(Configuration configuration, BoundSql boundSql, String sqlId) {
        StringBuilder str = new StringBuilder(100);
        str.append("sqlid:").append(sqlId);
        str.append(",sql:");
        String sql = "";
        try {
            sql = showSql(configuration, boundSql);
        } catch (Error e) {
            LOGGER.error("解析SQL异常", e);
        }
        str.append(sql);
        return str.toString();
    }

    private static String getParameterValue(Object obj) {
        String value = "";
        if (null == obj) {

        } else if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            value = obj.toString();
        }
        return value;
    }

    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (null == parameterMappings || parameterMappings.isEmpty())
            return sql;

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public static boolean isPrintSQL() {
        return printSQL;
    }

    public static void setPrintSQL(boolean printSQL) {
        MybatisLogInterceptor.printSQL = printSQL;
    }

}
