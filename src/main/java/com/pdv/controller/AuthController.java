package com.pdv.controller;

import com.pdv.dto.AuthResponseDTO;
import com.pdv.dto.LoginRequestDTO;
import com.pdv.model.Funcionario;
import com.pdv.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final FuncionarioService funcionarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          FuncionarioService funcionarioService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.funcionarioService = funcionarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getSenha())
            );
            Funcionario funcionario = funcionarioService.buscarPorLogin(request.getLogin())
                    .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

            AuthResponseDTO response = new AuthResponseDTO(funcionario.getId(), funcionario.getNome(), funcionario.getRole(), "local-session");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Login ou senha inválidos");
        }
    }
}
