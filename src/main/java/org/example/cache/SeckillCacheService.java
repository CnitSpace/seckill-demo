package org.example.cache;

import org.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SeckillCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 预热秒杀库存到Redis
     */
    public void preheatStock(Long activityId, Integer stockCount) {
        String key = RedisKeyUtil.getSeckillStockKey(activityId);
        redisTemplate.opsForValue().set(key, stockCount, 24, TimeUnit.HOURS);
    }

    /**
     * 预减库存
     */
    public Long decrStock(Long activityId, Integer count) {
        String key = RedisKeyUtil.getSeckillStockKey(activityId);
        return redisTemplate.opsForValue().decrement(key, count);
    }

    /**
     * 获取剩余库存
     */
    public Integer getStock(Long activityId) {
        String key = RedisKeyUtil.getSeckillStockKey(activityId);
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : (Integer) value;
    }
}