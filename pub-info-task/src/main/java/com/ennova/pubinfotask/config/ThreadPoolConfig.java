package com.ennova.pubinfotask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程数量，默认1
     */
    private int corePoolSize = 8;

    /**
     * 最大线程数量，默认Integer.MAX_VALUE;
     */
    private int maxPoolSize = 10;

    /**
     * 空闲线程存活时间
     */
    private int keepAliveSeconds = 60;

    /**
     * 线程阻塞队列容量,默认Integer.MAX_VALUE
     */
    private int queueCapacity = 1;

    /**
     * 是否允许核心线程超时
     */
    private boolean allowCoreThreadTimeOut = false;


    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        // 设置拒绝策略，直接在execute方法的调用线程中运行被拒绝的任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        return executor;
    }
}