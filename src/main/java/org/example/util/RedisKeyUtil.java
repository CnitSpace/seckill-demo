package org.example.util;

public class RedisKeyUtil {
    private static final String SECKILL_STOCK_PREFIX = "seckill:stock:";

    public static String getSeckillStockKey(Long activityId) {
        return SECKILL_STOCK_PREFIX + activityId;
    }

    public static String getRateLimitKey(String prefix, Object... args) {
        StringBuilder builder = new StringBuilder("rate:limit:");
        builder.append(prefix);
        for (Object arg : args) {
            if (arg != null) {
                builder.append(":").append(arg.toString());
            }
        }
        return builder.toString();
    }
}