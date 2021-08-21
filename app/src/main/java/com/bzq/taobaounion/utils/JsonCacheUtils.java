package com.bzq.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bzq.taobaounion.base.BaseApplication;
import com.bzq.taobaounion.model.DTO.CacheWithDuration;
import com.google.gson.Gson;

/**
 * @author Gerkey
 * Created on 2021/8/8
 * 实现历史记录缓存的工具
 */
public class JsonCacheUtils {
    private static JsonCacheUtils sJsonCacheUtils = null;
    private static final String JSON_CACHE_SP_NAME = "JsonCacheUtils";
    private final SharedPreferences mSp;
    private final Gson mGson;

    private JsonCacheUtils() {
        mSp = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public static JsonCacheUtils getInstance() {
        if (sJsonCacheUtils == null) {
            sJsonCacheUtils = new JsonCacheUtils();
        }
        return sJsonCacheUtils;
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSp.edit();
        String valueStr = mGson.toJson(value);
        if (duration != -1L) {
            // 当前时间
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void deleteCache(String key) {
        mSp.edit().remove(key).apply();
    }


    public <T> T getValue(String key, Class<T> clazz) {
        String valueWithDuration = mSp.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        // 对时间进行判断
        // 判断是否过期了
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            // 过期了
            return null;
        } else {
            // 没过期
            String cache = cacheWithDuration.getCache();
            return mGson.fromJson(cache, clazz);
        }
    }
}

