package org.jeecg.modules.ad.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 实体名称缓存工具类
 * 用于缓存实体ID和名称的映射关系，减少数据库访问
 */
@Component
public class EntityNameCache {
    // 广告ID到名称的缓存
    private final Map<String, CacheItem<String>> adNameCache = new ConcurrentHashMap<>();
    
    // 公司ID到名称的缓存
    private final Map<String, CacheItem<String>> companyNameCache = new ConcurrentHashMap<>();
    
    // 司机ID到名称的缓存
    private final Map<String, CacheItem<String>> driverNameCache = new ConcurrentHashMap<>();
    
    // 缓存过期时间（毫秒）
    private static final long CACHE_EXPIRE_TIME = TimeUnit.MINUTES.toMillis(10);
    
    /**
     * 获取广告名称
     * @param adId 广告ID
     * @return 缓存中的广告名称，如果不存在或已过期则返回null
     */
    public String getAdName(String adId) {
        return getValue(adNameCache, adId);
    }
    
    /**
     * 缓存广告名称
     * @param adId 广告ID
     * @param adName 广告名称
     */
    public void putAdName(String adId, String adName) {
        putValue(adNameCache, adId, adName);
    }
    
    /**
     * 获取公司名称
     * @param companyId 公司ID
     * @return 缓存中的公司名称，如果不存在或已过期则返回null
     */
    public String getCompanyName(String companyId) {
        return getValue(companyNameCache, companyId);
    }
    
    /**
     * 缓存公司名称
     * @param companyId 公司ID
     * @param companyName 公司名称
     */
    public void putCompanyName(String companyId, String companyName) {
        putValue(companyNameCache, companyId, companyName);
    }
    
    /**
     * 获取司机名称
     * @param driverId 司机ID
     * @return 缓存中的司机名称，如果不存在或已过期则返回null
     */
    public String getDriverName(String driverId) {
        return getValue(driverNameCache, driverId);
    }
    
    /**
     * 缓存司机名称
     * @param driverId 司机ID
     * @param driverName 司机名称
     */
    public void putDriverName(String driverId, String driverName) {
        putValue(driverNameCache, driverId, driverName);
    }
    
    /**
     * 从缓存中获取值
     * @param cache 缓存Map
     * @param key 键
     * @param <T> 值类型
     * @return 缓存值，如果不存在或已过期则返回null
     */
    private <T> T getValue(Map<String, CacheItem<T>> cache, String key) {
        CacheItem<T> item = cache.get(key);
        if (item == null) {
            return null;
        }
        
        // 检查是否过期
        if (System.currentTimeMillis() - item.getTimestamp() > CACHE_EXPIRE_TIME) {
            cache.remove(key);
            return null;
        }
        
        return item.getValue();
    }
    
    /**
     * 将值放入缓存
     * @param cache 缓存Map
     * @param key 键
     * @param value 值
     * @param <T> 值类型
     */
    private <T> void putValue(Map<String, CacheItem<T>> cache, String key, T value) {
        cache.put(key, new CacheItem<>(value, System.currentTimeMillis()));
    }
    
    /**
     * 缓存项，包含值和时间戳
     * @param <T> 值类型
     */
    private static class CacheItem<T> {
        private final T value;
        private final long timestamp;
        
        public CacheItem(T value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
        
        public T getValue() {
            return value;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
} 