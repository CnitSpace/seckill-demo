package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`order`")
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String orderNo;         // 订单编号
    private Long userId;            // 用户ID
    private Long goodsId;           // 商品ID
    private Long activityId;        // 活动ID
    private String goodsName;       // 商品名称
    private Integer goodsCount;     // 商品数量
    private BigDecimal goodsPrice;  // 商品价格
    private Integer orderChannel;   // 订单渠道
    private Integer status;         // 状态：0-未支付，1-已支付，2-已发货，3-已收货，4-已退款，5-已完成
    private Integer deliveryAddrId; // 收货地址ID
    private Date payTime;           // 支付时间
    private Date createTime;        // 创建时间
    private Date UpdateTime;        // 更新时间
}
