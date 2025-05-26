package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.SeckillActivity;

@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {
    @Update("UPDATE seckill_activity SET stock_count = stock_count - #{stockCount} " +
            "WHERE id = #{activityId} AND stock_count > 0")
    int reduceStock(@Param("activityId") Long activityId, @Param("stockCount") Integer stockCount);

    @Select("SELECT stock_count FROM seckill_activity WHERE id = #{activityId}")
    Integer getStockCount(@Param("activityId") Long activityId);
}