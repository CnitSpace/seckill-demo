CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE seckill;


CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` varchar(50) NOT NULL COMMENT '用户名',
                        `password` varchar(100) NOT NULL COMMENT '密码，加密存储',
                        `salt` varchar(50) NOT NULL COMMENT '加密盐值',
                        `phone` varchar(20) NOT NULL COMMENT '手机号',
                        `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                        `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
                        `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
                        `login_count` int NOT NULL DEFAULT '0' COMMENT '登录次数',
                        `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `idx_username` (`username`),
                        UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- 初始化用户数据
INSERT INTO `user` (username, password, salt, phone, email, avatar)
VALUES
    ('user1', 'e10adc3949ba59abbe56e057f20f883e', 'abc123', '13800138001', 'user1@example.com', 'https://example.com/avatar1.jpg'),
    ('user2', 'e10adc3949ba59abbe56e057f20f883e', 'def456', '13800138002', 'user2@example.com', 'https://example.com/avatar2.jpg');
    
  
  
  

CREATE TABLE `goods` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                         `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
                         `goods_title` varchar(100) NOT NULL COMMENT '商品标题',
                         `goods_img` varchar(255) NOT NULL COMMENT '商品图片URL',
                         `goods_detail` text COMMENT '商品详情',
                         `goods_price` decimal(10,2) NOT NULL COMMENT '商品原价',
                         `goods_stock` int NOT NULL DEFAULT '0' COMMENT '商品库存',
                         `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';


CREATE TABLE `seckill_activity` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
                                    `activity_name` varchar(100) NOT NULL COMMENT '活动名称',
                                    `goods_id` bigint NOT NULL COMMENT '商品ID',
                                    `seckill_price` decimal(10,2) NOT NULL COMMENT '秒杀价',
                                    `stock_count` int NOT NULL COMMENT '秒杀库存',
                                    `start_time` datetime NOT NULL COMMENT '开始时间',
                                    `end_time` datetime NOT NULL COMMENT '结束时间',
                                    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未开始，1-进行中，2-已结束',
                                    `limit_per_user` int NOT NULL DEFAULT '1' COMMENT '每人限购数量',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_goods_id` (`goods_id`),
                                    KEY `idx_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀活动表';

CREATE TABLE `order` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
                         `order_no` varchar(50) NOT NULL COMMENT '订单编号',
                         `user_id` bigint NOT NULL COMMENT '用户ID',
                         `goods_id` bigint NOT NULL COMMENT '商品ID',
                         `activity_id` bigint DEFAULT NULL COMMENT '活动ID',
                         `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
                         `goods_count` int NOT NULL DEFAULT '1' COMMENT '商品数量',
                         `goods_price` decimal(10,2) NOT NULL COMMENT '商品单价',
                         `order_channel` tinyint NOT NULL DEFAULT '1' COMMENT '渠道：1-PC,2-Android,3-iOS',
                         `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未支付，1-已支付，2-已发货，3-已收货，4-已退款，5-已完成',
                         `delivery_addr_id` bigint DEFAULT NULL COMMENT '收货地址ID',
                         `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `idx_order_no` (`order_no`),
                         KEY `idx_user_id` (`user_id`),
                         KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


CREATE TABLE `stock_flow` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水ID',
                              `goods_id` bigint NOT NULL COMMENT '商品ID',
                              `order_id` bigint DEFAULT NULL COMMENT '订单ID',
                              `activity_id` bigint DEFAULT NULL COMMENT '活动ID',
                              `change_amount` int NOT NULL COMMENT '变更数量(正为增加，负为减少)',
                              `current_stock` int NOT NULL COMMENT '变更后库存',
                              `operation_type` tinyint NOT NULL COMMENT '操作类型：1-入库，2-出库，3-秒杀扣减，4-库存回滚',
                              `operation_desc` varchar(255) DEFAULT NULL COMMENT '操作描述',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_goods_id` (`goods_id`),
                              KEY `idx_order_id` (`order_id`),
                              KEY `idx_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水表';

-- 初始化商品数据
INSERT INTO `goods` (goods_name, goods_title, goods_img, goods_detail, goods_price, goods_stock)
VALUES
    ('iPhone 15', 'Apple iPhone 15 128GB', 'https://example.com/iphone15.jpg', '最新款iPhone手机', 5999.00, 100),
    ('小米13', 'Xiaomi 13 256GB', 'https://example.com/xiaomi13.jpg', '小米旗舰手机', 3999.00, 200);

-- 初始化秒杀活动
INSERT INTO `seckill_activity` (activity_name, goods_id, seckill_price, stock_count, start_time, end_time, limit_per_user)
VALUES
    ('iPhone 15秒杀', 1, 4999.00, 10, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1),
    ('小米13秒杀', 2, 2999.00, 20, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY), 1);