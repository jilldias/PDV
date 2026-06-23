package com.pdv.auth;

import com.pdv.model.Funcionario;
import com.pdv.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final FuncionarioRepository funcionarioRepository;
    private final SessionInfo sessionInfo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            FuncionarioRepository funcionarioRepository,
            SessionInfo sessionInfo,
            PasswordEncoder passwordEncoder) {

        this.funcionarioRepository = funcionarioRepository;
        this.sessionInfo = sessionInfo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            return false;
        }

        Optional<Funcionario> funcionario =
                funcionarioRepository.findByLogin(login.trim());

        if (funcionario.isEmpty()) {
            return false;
        }

        Funcionario usuario = funcionario.get();

        if (!senhaValida(senha, usuario)) {
            return false;
        }

        sessionInfo.setAuthenticatedUser(usuario);

        return true;
    }

    private boolean senhaValida(String senhaDigitada, Funcionario usuario) {
        String senhaArmazenada = usuario.getSenha();
        if (senhaArmazenada == null || senhaArmazenada.isBlank()) {
            return false;
        }

        if (senhaArmazenada.startsWith("$2") && passwordEncoder.matches(senhaDigitada, senhaArmazenada)) {
            return true;
        }

        if (senhaArmazenada.equals(senhaDigitada)) {
            usuario.setSenha(passwordEncoder.encode(senhaDigitada));
            funcionarioRepository.save(usuario);
            return true;
        }

        return false;
    }
}
