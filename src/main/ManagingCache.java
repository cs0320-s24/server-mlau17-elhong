package main;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import main.ACSDataSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManagingCache {
    LoadingCache<String, BroadBandData> cache = CacheBuilder.newBuilder()
    private final LoadingCache<String, BroadBandData> cachedData = CacheBuilder.newBuilder()
            .maximumSize(50)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, BroadBandData>));

}
