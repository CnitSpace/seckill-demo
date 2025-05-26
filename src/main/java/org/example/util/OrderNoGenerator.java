package org.example.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String generate() {
        String timestamp = LocalDateTime.now().format(formatter);
        int seq = counter.incrementAndGet() % 10000;
        return "O" + timestamp + String.format("%04d", seq);
    }
}