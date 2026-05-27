package com.pdv.security;

import com.pdv.model.Funcionario;
import com.pdv.repository.FuncionarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;

    public CustomUserDetailsService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Funcionario não encontrado com login: " + username));
        return new CustomUserDetails(funcionario);
    }
}
