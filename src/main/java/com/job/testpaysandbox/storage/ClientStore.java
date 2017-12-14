package com.job.testpaysandbox.storage;

import com.job.testpaysandbox.model.Client;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 12/14/17.
 */
@Component
public class ClientStore {

    private final Map<String, Client> clients = new HashMap<>();

    @PostConstruct
    public void init() {
        clients.put("111", new Client("password", "test@email.ru"));
        clients.put("222", new Client("password2", "test2@email.ru"));
    }

    public Client getClientByClientId(String clientId) {
        return clients.get(clientId);
    }
}
