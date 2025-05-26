package org.example.service;

import org.example.entity.Order;

public interface SeckillService {
    /**
     * 秒杀请求处理
     */
    Order processSeckill(Long userId, Long activityId, Integer quantity);

    /**
     * 预热秒杀库存到Redis
     */
    void preheatSeckillStock(Long activityId);

    /**
     * 检查秒杀结果
     */
    Order checkSeckillResult(Long userId, Long activityId);
}