package com.pdv.auth;

import com.pdv.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class SessionInfo {

    private Funcionario authenticatedUser;

    public Funcionario getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(Funcionario authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public boolean hasAuthenticatedUser() {
        return authenticatedUser != null;
    }
}
