package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.StockFlow;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockFlowMapper extends BaseMapper<StockFlow> {

    // 注解方式实现
    @Select("SELECT * FROM stock_flow WHERE goods_id = #{goodsId} ORDER BY create_time DESC")
    List<StockFlow> selectByGoodsId(@Param("goodsId") Long goodsId);

    // XML方式实现
    List<StockFlow> selectByActivityId(@Param("activityId") Long activityId);

    // 复杂查询示例
    List<StockFlow> selectByConditions(
            @Param("goodsId") Long goodsId,
            @Param("activityId") Long activityId,
            @Param("operationType") Integer operationType,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    // 获取商品当前库存
    @Select("SELECT current_stock FROM stock_flow WHERE goods_id = #{goodsId} " +
            "ORDER BY create_time DESC LIMIT 1")
    Integer getCurrentStock(@Param("goodsId") Long goodsId);

    // 批量插入
    int batchInsert(@Param("list") List<StockFlow> stockFlows);
}