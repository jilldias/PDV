package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Funcionario;
import com.pdv.repository.FuncionarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Funcionario criarFuncionario(Funcionario funcionario) {
        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return funcionarioRepository.findById(id);
    }

    public Optional<Funcionario> buscarPorLogin(String login) {
        return funcionarioRepository.findByLogin(login);
    }

    public Funcionario atualizarFuncionario(Long id, Funcionario dadosAtualizados) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "id", id));

        funcionarioExistente.setNome(dadosAtualizados.getNome());
        funcionarioExistente.setCargo(dadosAtualizados.getCargo());
        funcionarioExistente.setLogin(dadosAtualizados.getLogin());
        funcionarioExistente.setRole(dadosAtualizados.getRole());

        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().trim().isEmpty()) {
            funcionarioExistente.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        return funcionarioRepository.save(funcionarioExistente);
    }

    public void removerFuncionario(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionario", "id", id);
        }
        funcionarioRepository.deleteById(id);
    }
}
