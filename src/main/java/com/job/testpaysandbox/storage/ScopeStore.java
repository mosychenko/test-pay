package com.job.testpaysandbox.storage;

import com.job.testpaysandbox.model.Role;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 12/14/17.
 */
@Component
public class ScopeStore {
    private final Map<Role, String> scopeForRole = new HashMap<>();

    @PostConstruct
    public void init(){
        scopeForRole.put(Role.USER, "/payments/.*");
        scopeForRole.put(Role.ADMIN, "/admin/.*");
    }

    public String getScopeForRole(Role role) {
        return scopeForRole.get(role);
    }
}
