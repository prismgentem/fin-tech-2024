package org.example.crudkudago.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return Executors.newFixedThreadPool(threadPoolSize, runnable -> {
            var thread = new Thread(runnable);
            thread.setName("FixedThreadPool-Worker");
            return thread;
        });
    }

    @Bean(name = "scheduledThreadPool")
    public ScheduledExecutorService scheduledThreadPool() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize, runnable -> {
            var thread = new Thread(runnable);
            thread.setName("ScheduledThreadPool-Worker");
            return thread;
        });
    }
}
