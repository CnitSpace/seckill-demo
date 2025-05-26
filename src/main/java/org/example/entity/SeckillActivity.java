package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀活动实体类
 */
@Data
@TableName("`seckill_activity`")
public class SeckillActivity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                // 活动ID
    private String activityName;    // 活动名称
    private Long goodsId;           // 商品ID
    private BigDecimal seckillPrice; // 秒杀价
    private Integer stockCount;     // 秒杀库存
    private Date startTime;         // 开始时间
    private Date endTime;           // 结束时间
    private Integer status;         // 状态：0-未开始，1-进行中，2-已结束
    private Integer limitPerUser;   // 每人限购数量
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间
}