package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "Seckill system is running on port 8080";
    }

    @GetMapping("/info")
    public String info() {
        return """
                {
                    "status": "UP",
                    "service": "Seckill System",
                    "port": 8080,
                    "timestamp": "%s"
                }
                """.formatted(java.time.LocalDateTime.now());
    }
}