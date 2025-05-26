# seckill-demo

> 此项目为学生教学应用demo，实现秒杀商品系统，欢迎有兴趣研究的小伙伴参于项目的更新。

# 项目核心需求

## 业务需求

1. 用户端：秒杀活动页面快速加载、秒杀请求即时响应、库存实时更新
2. 商家端：秒杀活动配置、库存管理、数据监控
3. 系统端：抗瞬时流量冲击、防止超卖、保证数据最终一致性

## 功能需求
1. 用户模块：登录认证、活动列表、秒杀请求提交
2. 秒杀模块：库存预加载、请求排队、秒杀结果反馈
3. 库存管理：Redis预减库存+MySQL最终扣减
4. 订单模块：异步订单生成、支付状态同步
5. 限流降级：请求队列控制、熔断机制

## 非功能需求
1. 并发能力：支持10万级QPS
2. 响应时间：核心接口<200ms
3. 数据一致性：库存误差<0.1%
4. 容灾能力：单节点故障自动转移

## 测试方案
1. 压力测试
   - JMeter模拟10万用户并发
   - 监控指标：TPS、RT、错误率
2. 一致性测试
   - 并发扣减库存验证
   - 断网场景测试
3. 异常测试
   - Redis节点宕机
   - MySQL主从延迟


# 项目实施信息

## 项目版本
- idea版本 2024.1
- jdk 17
- spring boot 3.0.7
- mysql 8.0
- redis 3.0.504

## 当前技术栈  (已实现 ✅ 未实现 ❌)
  - 后端: Spring Boot ✅、 Spring MVC ✅、MyBatis+ ✅、AOP ❌
  - 中间件: Redis ✅、RabbitMQ ❌
  - 数据库: MySQL ✅
  - 分布式锁：Redisson ✅
  - 前端: Thymeleaf ❌、jQuery ❌、Bootstrap ❌
  - 其他: Lombok ✅、Hutool ✅、jwt ✅


# 接口验证

- 查看系统
```
curl --request GET \
  --url http://localhost:8080/api/test/info \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0'
```

- 查看端口
```
  curl --request GET \
  --url http://localhost:8080/api/test/ping \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0'
```

- 注册用户
```
curl --request POST \
  --url http://localhost:8080/api/user/register \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Content-Type: application/json' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0' \
  --data '{
    "username": "testuser",
    "password": "Test1234",
    "phone": "13800138003",
    "email": "test@example.com"
}'
```

- 查询用户
```
curl --request GET \
  --url http://localhost:8080/api/user/testuser \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0'
```


- 用户认证登入
```
curl --request POST \
  --url http://localhost:8080/api/auth/login \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Content-Type: application/json' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0' \
  --data '{
    "username": "testuser",
    "password": "Test1234"
}'
```

- 秒杀商品预热
```
curl --request POST \
  --url http://localhost:8080/api/seckill/preheat/1 \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0'
```

- 用户下单秒杀商品
```
curl --request POST \
  --url http://localhost:8080/api/seckill/1/1 \
  --header 'Accept: */*' \
  --header 'Accept-Encoding: gzip, deflate, br' \
  --header 'Cache-Control: no-cache' \
  --header 'Connection: keep-alive' \
  --header 'Host: localhost:8080' \
  --header 'User-Agent: PostmanRuntime-ApipostRuntime/1.1.0' \
  --header 'userId: 1'
```