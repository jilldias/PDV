package com.pdv.auth;

import com.pdv.model.Funcionario;
import com.pdv.repository.FuncionarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository funcionarioRepository;
    private final SessionInfo sessionInfo;

    public AuthService(AuthenticationManager authenticationManager,
                       FuncionarioRepository funcionarioRepository,
                       SessionInfo sessionInfo) {
        this.authenticationManager = authenticationManager;
        this.funcionarioRepository = funcionarioRepository;
        this.sessionInfo = sessionInfo;
    }

    public Funcionario authenticate(String login, String senha) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(login, senha);

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            if (authentication.isAuthenticated()) {
                Funcionario funcionario = funcionarioRepository.findByLogin(login)
                        .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
                sessionInfo.setAuthenticatedUser(funcionario);
                return funcionario;
            }
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Login ou senha inválidos", e);
        }

        throw new IllegalArgumentException("Falha ao autenticar funcionário");
    }

    public void logout() {
        sessionInfo.setAuthenticatedUser(null);
    }
}
