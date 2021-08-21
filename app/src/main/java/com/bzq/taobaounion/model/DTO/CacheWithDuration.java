package com.bzq.taobaounion.model.DTO;

/**
 * @author Gerkey
 * Created on 2021/8/8
 */
public class CacheWithDuration {
    private long duration;
    private String cache;

    public CacheWithDuration(long duration, String valueStr) {
        this.duration = duration;
        this.cache = valueStr;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}

