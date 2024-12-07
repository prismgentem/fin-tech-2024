package org.example.crudkudago.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ThreadPoolConfig {

    @Value("${app.thread-pool.size}")
    private int threadPoolSize;

    @Value("${app.scheduled.thread-pool.size}")
    private int scheduledThreadPoolSize;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(threadPoolSize,
                new BasicThreadFactory.Builder()
                        .namingPattern("FixedThreadPool-Worker-%d")
                        .daemon(true)
                        .build());
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize,
                new BasicThreadFactory.Builder()
                        .namingPattern("ScheduledThreadPool-Worker-%d")
                        .daemon(true)
                        .build());
    }
}
