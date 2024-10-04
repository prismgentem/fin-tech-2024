package org.example.logexecutionstarter.config;

import org.example.logexecutionstarter.aspect.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LogExecutionAutoConfiguration {
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
