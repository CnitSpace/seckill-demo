package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 库存流水实体类
 */
@Data
@TableName("stock_flow")
public class StockFlow {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                // 流水ID

    private Long goodsId;           // 商品ID
    private Long activityId;        // 活动ID(可为空)
    private Long orderId;           // 订单ID(可为空)
    private Integer changeAmount;    // 变更数量(正为增加，负为减少)
    private Integer currentStock;   // 变更后库存
    private Integer operationType;  // 操作类型：1-入库，2-出库，3-秒杀扣减，4-库存回滚
    private String operationDesc;   // 操作描述
    private Date createTime;        // 创建时间

    // 业务字段(非数据库字段)
    //private transient String goodsName; // 商品名称(用于展示)
}