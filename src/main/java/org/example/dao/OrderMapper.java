package org.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    // 自定义方法示例
    @Select("SELECT COUNT(1) FROM `order` " +
            "WHERE user_id = #{userId} AND activity_id = #{activityId}")
    boolean exists(@Param("userId") Long userId,
                   @Param("activityId") Long activityId);

    @Select("SELECT * FROM `order` " +
            "WHERE user_id = #{userId} AND activity_id = #{activityId}")
    Order selectByUserAndActivity(@Param("userId") Long userId,
                                  @Param("activityId") Long activityId);
}