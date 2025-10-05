package com.farmtastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// 啟用 Redis Session 儲存機制
// maxInactiveIntervalInSeconds 設定 Session 的最長閒置時間 (秒)，
// 覆蓋 application.properties 中的設定，這裡設定 1 小時 (3600 秒)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) 
@Configuration
public class RedisSessionConfig {
    // 這裡通常不需要額外的程式碼，Spring Boot 會自動配置 RedisTemplate 和 Session Repository
    // 只需要這個註釋 (Annotation) 就可以讓 Spring Session 開始工作
}