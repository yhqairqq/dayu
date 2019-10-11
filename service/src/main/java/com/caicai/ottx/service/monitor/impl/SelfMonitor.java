package com.caicai.ottx.service.monitor.impl;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.alibaba.otter.shared.common.utils.thread.NamedThreadFactory;
import com.caicai.ottx.service.monitor.Monitor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * jvm内自动运行，不需要通过外部定时触发
 *
 * @since 4.2.2
 */
public class SelfMonitor implements Monitor, InitializingBean, DisposableBean {

    protected static final Logger log          = LoggerFactory.getLogger("monitorInfo");
    private static final int         DEFAULT_POOL = 1;
    private ScheduledExecutorService executor;
    private ScheduledFuture          future;
    private GlobalMonitor monitor;
    private AtomicBoolean            enable       = new AtomicBoolean(true);
    private int                      interval     = 10;

    public void explore() {
        monitor.explore();
    }

    public void explore(Long... pipelineIds) {
        monitor.explore(pipelineIds);
    }

    public void explore(List<AlarmRule> rules) {
        monitor.explore(rules);
    }

    public void destroy() throws Exception {
        if (enable.get()) {
            stop();
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (enable.get()) {
            start();
        }
    }

    private synchronized void start() {
        if (executor == null) {
            executor = new ScheduledThreadPoolExecutor(DEFAULT_POOL, new NamedThreadFactory("Self-Monitor"),
                                                       new ThreadPoolExecutor.CallerRunsPolicy());
        }
        if (future == null) {
            future = executor.scheduleWithFixedDelay(new Runnable() {

                public void run() {
                    try {
//                        System.out.println("监控心跳+"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        monitor.explore();// 定时调用
                    } catch (Exception e) {
                        log.error("self-monitor failed.", e);
                    }
                }
            }, interval, interval, TimeUnit.SECONDS);
        }
    }

    private synchronized void stop() {
        if (future != null) {
            future.cancel(true);
        }

        if (executor != null) {
            try {
                executor.awaitTermination(2 * 1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    public void setMonitor(GlobalMonitor monitor) {
        this.monitor = monitor;
    }

    public void setEnable(boolean enable) {
        this.enable.set(enable);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
