package edu.brown.cs.student.main.server;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class ACSCacheData {
    public Cache<String, String> customizableCache(int maximumSize, int minuteDelete) {
        Cache<String, String> makeCache =
                CacheBuilder.newBuilder()
                        .maximumSize(maximumSize)
                        .expireAfterWrite(minuteDelete, TimeUnit.MINUTES)
                        .build();
        return makeCache;
    }


}
