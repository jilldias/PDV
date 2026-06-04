package com.pdv.auth;

import com.pdv.model.Funcionario;
import com.pdv.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final SessionInfo sessionInfo;

    public AuthService(
            FuncionarioRepository funcionarioRepository,
            SessionInfo sessionInfo) {

        this.funcionarioRepository = funcionarioRepository;
        this.sessionInfo = sessionInfo;
    }

    public boolean login(String login, String senha) {

        Optional<Funcionario> funcionario =
                funcionarioRepository.findByLogin(login);

        if (funcionario.isEmpty()) {
            return false;
        }

        Funcionario usuario = funcionario.get();

        if (!usuario.getSenha().equals(senha)) {
            return false;
        }

        sessionInfo.setAuthenticatedUser(usuario);

        return true;
    }
}