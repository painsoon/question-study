package com.sheca.unitrust.common.util.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author liujida
 * create: 2019-05-13
 */
@Component
public class CacheUtil {

    @Autowired
    private CacheManager cacheManager;

    public void put(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);

        cache.put(key, value);
    }

    public Cache.ValueWrapper get(String cacheName, String key) {
        Cache cache = getCache(cacheName);

        return cache.get(key);
    }

    private Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            throw new NoSuchCacheException("没有找到 " + cacheName + " 对应的cache");
        }

        return cache;
    }
}
