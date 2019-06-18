package com.allen.rbac.util;

import com.alibaba.fastjson.JSON;
import com.allen.rbac.dto.SysUserDto;
import com.allen.rbac.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    private static CopyStrategy strategy = CopyStrategy.CGLIB;

    public enum CopyStrategy {
        FastJson, Spring_Reflect, CGLIB
    }

    /**
     *
     * @param source
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> t) {
        if(null == source) {
            LOGGER.warn("source is null");
            return null;
        }

        try{
            switch (strategy) {
                case FastJson:
                    String jsonString = JSON.toJSONString(source);
                    return JSON.parseObject(jsonString, t);
                case Spring_Reflect:
                case CGLIB:
                    T target = t.newInstance();
                    copyProperties(source, target);
                    return target;
                default:
                    break;
            }
        } catch(Exception e) {
            LOGGER.error("对象转换错误：", e);
        }
        return null;
    }

    /**
     *
     * @param source
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> copyProperties(List<?> source, Class<T> t) {
        if(CollectionUtils.isEmpty(source)) {
            LOGGER.warn("source is empty");
            return Collections.emptyList();
        }

        List<T> tList = new ArrayList<>();
        for(int i = 0; i < source.size(); i++) {
            tList.add(copyProperties(source.get(i), t));
        }
        return tList;
    }

    /**
     * 通过spring转换
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if(null == source || null == target) {
            LOGGER.warn("source and target can not be null");
            return;
        }

        try{
            switch (strategy) {
                case Spring_Reflect:
                    org.springframework.beans.BeanUtils.copyProperties(source, target);
                    break;
                case CGLIB:
                    BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
                    beanCopier.copy(source, target, null);
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LOGGER.error("对象转换错误：", e);
        }
    }

    public static void main(String...args) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("allen");
        sysUser.setPassword("123456");
        sysUser.setStatus(1);

        long s = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++) {
            sysUser.setId(new Long(i));
            copyProperties(sysUser, SysUserDto.class);
        }
        long e = System.currentTimeMillis();

        System.out.println(e - s);
    }
}
