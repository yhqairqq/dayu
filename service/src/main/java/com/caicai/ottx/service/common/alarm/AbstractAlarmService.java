package com.caicai.ottx.service.common.alarm;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by huaseng on 2019/8/23.
 */
public abstract class AbstractAlarmService implements AlarmService, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAlarmService.class);

    private BlockingQueue<AlarmMessage> queue  = new LinkedBlockingQueue<AlarmMessage>(3 * 3 * 3600);
    private ExecutorService executor;
    private int                         period = 150;                                                // milliseconds

    public void sendAlarm(AlarmMessage data) {
        try {
            if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                logger.error(String.format("alarm sent to queue error : [%s]", data.toString()));
            }
        } catch (Exception e) {
            logger.error(String.format("send alarm [%s] to drgoon agent error!", data.toString()), e);
        }
    }

    private void sendAlarmInternal() {
        AlarmMessage data = null;
        try {
            data = queue.take();
            doSend(data);
            logger.info(String.format("has sent alarm [%s] to drgoon agent.", data.toString()));
        } catch (InterruptedException e) {
            logger.warn("otter-sendAlarm-worker was interrupted", e);
        } catch (Exception e) {
            logger.error(String.format("send alarm [%s] to drgoon agent error!", data.toString()), e);
        }
    }

    protected abstract void doSend(AlarmMessage data) throws Exception;

    public void afterPropertiesSet() throws Exception {
        executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable() {

            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    sendAlarmInternal();
                    LockSupport.parkNanos(period * 1000L * 1000L);
                }
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
        if (!queue.isEmpty()) {
            int size = queue.size();
            logger.warn(String.format("there are %d alarms wait to be sent \n %s", size, dumpQueue()));
        }
    }

    protected String dumpQueue() {
        if (queue.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (AlarmMessage data : queue) {
            sb.append(data.toString()).append("\n");
        }

        return sb.toString();
    }

    // ============= setter ===============

    public void setPeriod(int period) {
        this.period = period;
    }

}
