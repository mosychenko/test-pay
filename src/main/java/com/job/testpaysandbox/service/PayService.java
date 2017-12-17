package com.job.testpaysandbox.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.job.testpaysandbox.model.*;
import com.job.testpaysandbox.storage.MerchantAccountStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PayService {
    private static final Logger logger = Logger.getLogger(PayService.class.getName());

    private static final int ID_LENGTH = 24;
    private static final int QUEUE_CAPACITY = 50;
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private final Queue<PaymentPayload> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
    private AtomicBoolean queueFull = new AtomicBoolean();
    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("payment-executor")
            .setDaemon(true)
            .build();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, threadFactory);
    private AtomicBoolean shutdown = new AtomicBoolean();

    @Autowired
    private WebhookHandler webhookHandler;

    @Autowired
    private MerchantAccountStore accountStore;

    @PostConstruct
    public void init() {
        executor.scheduleWithFixedDelay(this::processing, 5, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        shutdown.set(true);
        executor.shutdownNow();
    }

    public PaymentResult addPaymentToProcessing(Payment payment) {
        if (queueFull.get()) {
            return PaymentResult.builder()
                    .state(PaymentProcessingStatus.FAILED.name().toLowerCase())
                    .build();
        }
        Account account = accountStore.getAccountByEmail(payment.getPayer().getEmail());
        PaymentPayload payload = PaymentPayload.builder()
                .id(Utils.generateRandomString(ID_LENGTH, true))
                .createDate(new Date())
                .payment(payment)
                .account(account)
                .build();
        try {
            queue.add(payload);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, "payment queue is full.", e);
            queueFull.set(true);
            return PaymentResult.builder()
                    .state(PaymentProcessingStatus.FAILED.name().toLowerCase())
                    .build();
        }

        return PaymentResult.builder()
                .id(payload.getId())
                .createTime(formatter.format(payload.getCreateDate()))
                .state(PaymentProcessingStatus.CREATED.name().toLowerCase())
                .build();
    }

    private void processing() {
        while (!shutdown.get()) {
            if (webhookHandler.full.get()) {
                return;
            }
            PaymentPayload payload = queue.poll();
            if (payload == null) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "pay processing sleep interrupted", e);
            }
            Account account = payload.getAccount();
            Amount amount = payload.getPayment().getTransaction().getAmount();
            PaymentProcessingStatus paymentProcessingStatus = PaymentProcessingStatus.FAILED;
            if (account.currency.equals(amount.getCurrency())) {
                BigDecimal amountValue = new BigDecimal(amount.getValue());
                if (account.totalMoney.compareTo(amountValue) >= 0) {
                    Account newAccount = new Account(account.currency, account.totalMoney.subtract(amountValue), account.secret);
                    accountStore.updateAccount(payload.getPayment().getPayer().getEmail(), newAccount);
                    paymentProcessingStatus = PaymentProcessingStatus.APPROVED;
                }
            }
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(account.secret.getBytes(StandardCharsets.UTF_8));
                String secret = new String(hash).toUpperCase();
                webhookHandler.sendEvent(payload, paymentProcessingStatus, secret);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            queueFull.compareAndSet(true, false);
        }
    }

}
