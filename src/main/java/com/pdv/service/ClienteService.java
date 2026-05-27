package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Cliente;
import com.pdv.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente criarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "cpf", cpf));
    }

    public Cliente atualizarCliente(Long id, Cliente dadosAtualizados) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
        clienteExistente.setNome(dadosAtualizados.getNome());
        clienteExistente.setCpf(dadosAtualizados.getCpf());
        clienteExistente.setTelefone(dadosAtualizados.getTelefone());
        clienteExistente.setEmail(dadosAtualizados.getEmail());
        clienteExistente.setEndereco(dadosAtualizados.getEndereco());
        return clienteRepository.save(clienteExistente);
    }

    public void removerCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", "id", id);
        }
        clienteRepository.deleteById(id);
    }
}
