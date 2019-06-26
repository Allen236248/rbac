package com.allen.rbac.shiro;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class RedisShiroCacheManager extends AbstractCacheManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected Cache createCache(String name) throws CacheException {
        return new RedisShiroCache(name, redisTemplate);
    }

}
