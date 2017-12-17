package com.job.testpaysandbox.controller;

import com.google.gson.Gson;
import com.job.testpaysandbox.model.WebhookNotificationMessage;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TestWebhookController {

    private Gson gson = new Gson();

    private Map<String, WebhookNotificationMessage> messages = new ConcurrentHashMap<>();

    @RequestMapping(value = "/webhook/test", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getWebhook(@RequestBody WebhookNotificationMessage message) {
        messages.put(message.id, message);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/webhook/test/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> findAllMessage() {
        return ResponseEntity.ok(gson.toJson(messages.values()));
    }
}
