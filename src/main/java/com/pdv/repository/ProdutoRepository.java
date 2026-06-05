package com.pdv.repository;

import com.pdv.model.Produto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Produto> findAll();

    @EntityGraph(attributePaths = "categoria")
    Optional<Produto> findByCodigoBarras(String codigoBarras);

    @EntityGraph(attributePaths = "categoria")
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaNomeIgnoreCase(String nomeCategoria);
}
