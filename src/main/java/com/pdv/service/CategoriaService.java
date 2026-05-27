package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Categoria;
import com.pdv.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria atualizarCategoria(Long id, Categoria dadosAtualizados) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
        categoriaExistente.setNome(dadosAtualizados.getNome());
        categoriaExistente.setDescricao(dadosAtualizados.getDescricao());
        return categoriaRepository.save(categoriaExistente);
    }

    public void removerCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", "id", id);
        }
        categoriaRepository.deleteById(id);
    }

    public Optional<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.findByNomeIgnoreCase(nome);
    }
}
