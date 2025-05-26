package org.example.controller;

import org.example.entity.Order;
import org.example.service.SeckillService;
import org.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀接口
     */
    @PostMapping("/{activityId}/{quantity}")
    public ResultVO<Order> seckill(
            @RequestHeader("userId") Long userId,
            @PathVariable Long activityId,
            @PathVariable Integer quantity) {
        try {
            Order order = seckillService.processSeckill(userId, activityId, quantity);
            return ResultVO.success(order);
        } catch (RuntimeException e) {
            // 直接使用异常中的消息
            return ResultVO.fail(e.getMessage());
        }
    }

    /**
     * 预热库存
     */
    @PostMapping("/preheat/{activityId}")
    public ResultVO<String> preheat(@PathVariable Long activityId) {
        seckillService.preheatSeckillStock(activityId);
        return ResultVO.success("预热成功");
    }

    /**
     * 查询秒杀结果
     */
    @GetMapping("/result/{activityId}")
    public ResultVO<Order> getResult(
            @RequestHeader("userId") Long userId,
            @PathVariable Long activityId) {
        Order order = seckillService.checkSeckillResult(userId, activityId);
        if (order == null) {
            return ResultVO.fail("秒杀结果未产生");
        }
        return ResultVO.success(order);
    }
}