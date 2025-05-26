package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootApplication

public class SeckillApplication {

    public static void main(String[] args) {
        // 启动应用并记录启动时间
        long startTime = System.currentTimeMillis();
        SpringApplication.run(SeckillApplication.class, args);
        long endTime = System.currentTimeMillis();

        // 打印启动信息
        System.out.printf("\n----------------------------------------------------------\n" +
                        "Application %s is running! Access URLs:\n" +
                        "Local: \t\thttp://localhost:8080/\n" +
                        "Startup Time: \t%d ms\n" +
                        "----------------------------------------------------------\n",
                "Seckill System", (endTime - startTime));
    }

    @Bean
    public CommandLineRunner checkDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // 获取数据库元数据
                DatabaseMetaData metaData = connection.getMetaData();

                // 输出连接成功信息
                System.out.println("✅ 数据库连接成功!");
                System.out.println("====================");
                System.out.println("数据库产品: " + metaData.getDatabaseProductName());
                System.out.println("数据库版本: " + metaData.getDatabaseProductVersion());
                System.out.println("数据库名称: " + connection.getCatalog()); // 获取当前数据库名称
                System.out.println("JDBC驱动: " + metaData.getDriverName());
                System.out.println("JDBC版本: " + metaData.getDriverVersion());
                System.out.println("连接URL: " + metaData.getURL());
                System.out.println("用户名: " + metaData.getUserName());
                System.out.println("====================");

            } catch (SQLException e) {
                System.err.println("❌ 数据库连接失败!");
                System.err.println("错误信息: " + e.getMessage());
                throw new RuntimeException("数据库连接失败", e);
            }
        };
    }

    // 密码加密配置
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}