package com.job.testpaysandbox.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.job.testpaysandbox.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by max on 12/17/17.
 */
@Service
public class WebhookHandler {

    private static final Logger logger = Logger.getLogger(WebhookHandler.class.getName());

    private static final int QUEUE_CAPACITY = 50;
    private static final int MAX_TRY_SEND_COUNT = 25;
    private static final int MAX_TRY_DAY_SEND = 3;

    private final Queue<WebhookPayload> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    public final AtomicBoolean full = new AtomicBoolean();
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("webhook-executor")
            .setDaemon(true)
            .build();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, threadFactory);
    private AtomicBoolean shutdown = new AtomicBoolean();
    private RestTemplate restTemplate;
    private int dayTryCount;

    @PostConstruct
    public void init() {
        dayTryCount = (MAX_TRY_SEND_COUNT + MAX_TRY_DAY_SEND - 1) / MAX_TRY_DAY_SEND;
        executor.scheduleWithFixedDelay(this::processing, 5, 5, TimeUnit.SECONDS);
        restTemplate = new RestTemplate();
    }

    @PreDestroy
    public void destroy() {
        shutdown.set(true);
        executor.shutdownNow();
    }

    public void sendEvent(PaymentPayload paymentPayload, PaymentProcessingStatus status, String secret) throws Exception {
        int currentCapacity = QUEUE_CAPACITY - queue.size();
        if (currentCapacity < 1) {
            throw new RuntimeException("webhook queue is full");
        }
        Amount amount = paymentPayload.getPayment().getTransaction().getAmount();
        WebhookNotificationMessage message = WebhookNotificationMessage.builder()
                .currency(amount.getCurrency())
                .amount(amount.getValue())
                .id(paymentPayload.getId())
                .externalId(paymentPayload.getPayment().getTransaction().getExternalId())
                .status(status)
                .build(secret);
        WebhookPayload webhookPayload = WebhookPayload.builder()
                .notificationUrl(paymentPayload.getPayment().getNotificationUrl())
                .message(message)
                .build();
        if (currentCapacity == 2) {
            queue.add(webhookPayload);
            full.set(true);
        } else {
            queue.add(webhookPayload);
        }
    }

    private void processing() {
        int handledMassage = 0;
        while (!shutdown.get()) {
            WebhookPayload payload = queue.poll();
            if (payload == null || payload.getTotalTryCount() > MAX_TRY_SEND_COUNT) {
                return;
            }
            if (payload.getTodayTryCount() > dayTryCount) {
                handledMassage++;
                queue.add(payload);
                // all message was handled and reschedule on next day
                if (handledMassage == queue.size()) {
                    return;
                }
                continue;
            }
            handledMassage = 0;
            try {
                restTemplate.postForLocation(payload.getNotificationUrl(), payload.getMessage());
                full.weakCompareAndSet(true, false);
            } catch (Exception e) {
                logger.log(Level.INFO, e.getMessage(), e);
                payload.incTry();
                queue.add(payload);
            }
        }
    }
}
