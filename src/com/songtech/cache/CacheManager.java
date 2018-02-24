package com.songtech.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create By YINN on 2018/2/9 13:59
 * Description :
 */
public class CacheManager {

    private static Map<String,Object> cache = new ConcurrentHashMap<String,Object>();

    public static Object getValue(String key) {
        return cache.get(key);
    }

    public static void putCache(String k, Object o) {
        cache.put(k, o);
    }

    public static void removeCache(String key) {
        // 根据 key 来删除缓存中的一条记录
        if(cache.containsKey(key)) {
            cache.remove(key);
        }
    }

    public static void clearCache() {
        // 清空缓存中的所有记录
        cache.clear();
    }

}
