package org.example.service.impl;

import org.example.dao.*;
import org.example.entity.*;
import org.example.service.SeckillService;
import org.example.cache.SeckillCacheService;
import org.example.util.OrderNoGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeckillCacheService seckillCacheService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StockFlowMapper stockFlowMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    private static final String SECKILL_LOCK_PREFIX = "seckill:lock:";
    private static final long LOCK_EXPIRE_TIME = 10; // 秒


    @Override
    @Transactional
    public Order processSeckill(Long userId, Long activityId, Integer quantity) {

        //1. 获取分布式锁
        RLock lock = redissonClient.getLock(SECKILL_LOCK_PREFIX + activityId);
        try {
            // 尝试加锁，最多等待1秒，锁有效期10秒
            if (!lock.tryLock(1, LOCK_EXPIRE_TIME, TimeUnit.SECONDS)) {
                throw new RuntimeException("当前参与人数过多，请稍后再试");
            }

            // 2. 检查是否已经秒杀过
            if (orderMapper.exists(userId, activityId)) {
                throw new RuntimeException("您已经参与过本次秒杀");
            }

            // 3. 检查是否超额数量
            if (quantity > seckillActivityMapper.selectById(activityId).getLimitPerUser()) {
                throw new RuntimeException("超额每人限购数量");
            }

            // 4. 从Redis预减库存
            Long stock = seckillCacheService.decrStock(activityId, quantity);
            if (stock == null || stock < 0) {
                throw new RuntimeException("秒杀活动已结束");
            }

            // 5. 创建订单
            Order order = createOrder(userId, activityId, quantity);

            // 6. 付款接口
            String payResult = asyncProcessPay(order);

            // 7. 真实扣除库存
            deductStock(order, payResult);

            // 8. 异步处理其他逻辑（如发送通知等）
            asyncProcessAfterSeckill(order);

            return order;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("秒杀失败，请重试");
        } finally {
            // 更新redis库存
            int stockCount = seckillActivityMapper.getStockCount(activityId);
            seckillCacheService.preheatStock(activityId, stockCount);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }

        }
    }

    @Override
    public void preheatSeckillStock(Long activityId) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null) {
            throw new RuntimeException("秒杀活动不存在");
        }
        activity.setStatus(1);
        seckillActivityMapper.updateById(activity);
        // 缓存库存到redis
        seckillCacheService.preheatStock(activityId, activity.getStockCount());
    }

    @Override
    public Order checkSeckillResult(Long userId, Long activityId) {
        return orderMapper.selectByUserAndActivity(userId, activityId);
    }

    private Order createOrder(Long userId, Long activityId, Integer quantity) {
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        Goods goods = goodsMapper.selectById(activity.getGoodsId());
        // 1. 创建订单记录
        Order order = new Order();
        order.setOrderNo(OrderNoGenerator.generate());
        order.setUserId(userId);
        order.setGoodsId(activity.getGoodsId());
        order.setActivityId(activityId);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(quantity);
        order.setGoodsPrice(activity.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateTime(new Date());
        orderMapper.insert(order);

        return order;
    }

    private void deductStock(Order order, String payResult) {
        if (payResult.equals("success")) {
            // 扣减秒杀活动的库存
            int affectedRows = seckillActivityMapper.reduceStock(order.getActivityId(), order.getGoodsCount());
            if (affectedRows == 0) {
                throw new RuntimeException("库存不足");
            }

            int stockCount = seckillActivityMapper.getStockCount(order.getActivityId());
            // 记录库存流水
            StockFlow stockFlow = new StockFlow();
            stockFlow.setGoodsId(order.getGoodsId());
            stockFlow.setActivityId(order.getActivityId());
            stockFlow.setOrderId(order.getId());
            stockFlow.setChangeAmount(-order.getGoodsCount());
            stockFlow.setOperationType(3); // 秒杀扣减
            stockFlow.setOperationDesc("用户秒杀扣减库存");
            stockFlow.setCurrentStock(stockCount);
            stockFlowMapper.insert(stockFlow);
        }
    }

    public String asyncProcessPay(Order order) {
        boolean result;
        // 付款时间3秒
        for (int i = 3; i > 0; i--) {
            try {
                Thread.sleep(1000); // 休眠1秒
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(i);
        }
        // 接口返还result
        result = true;
        if (result) {
            // 更新付款状态
            order.setStatus(1);
            order.setPayTime(new Date());
            order.setUpdateTime(new Date());
            orderMapper.updateById(order);
        }

        return "success";
    }

    @Async
    protected void asyncProcessAfterSeckill(Order order) {
        // 异步处理逻辑，如发送通知、更新缓存等
        // 可以使用@Async注解或消息队列实现
    }

}