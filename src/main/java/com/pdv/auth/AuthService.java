package com.pdv.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean authenticate(String login, String senha) {
        return true;
    }

    public void logout() {
    }
}
