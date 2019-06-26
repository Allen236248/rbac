package com.allen.rbac.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisShiroCache<K, V> implements Cache<K, V> {

    private String cacheKeyPrefix;

    private RedisTemplate<K, V> redisTemplate;

    private long expire = 8 * 60 * 60;

    public RedisShiroCache(String cacheKeyPrefix, RedisTemplate redisTemplate) {
        this.cacheKeyPrefix = cacheKeyPrefix + ":";
        this.redisTemplate = redisTemplate;
    }

    private K cacheKey(K suffix) {
        return (K) (cacheKeyPrefix + suffix);
    }

    @Override
    public V get(K k) throws CacheException {
        return redisTemplate.opsForValue().get(cacheKey(k));
    }

    @Override
    public V put(K k, V v) throws CacheException {
        V ov = get(k);
        // TODO 过期时间？
        redisTemplate.opsForValue().set(cacheKey(k), v, expire, TimeUnit.SECONDS);
        return ov;
    }

    @Override
    public V remove(K k) throws CacheException {
        V ov = get(k);
        redisTemplate.delete(cacheKey(k));
        return ov;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.delete(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys(cacheKey((K) "*"));
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for(K k : set) {
            list.add(get(k));
        }
        return list;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
