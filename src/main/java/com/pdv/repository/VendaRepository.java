package com.pdv.repository;

import com.pdv.model.Caixa;
import com.pdv.model.Venda;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Override
    @EntityGraph(attributePaths = {"funcionario", "caixa", "itens", "itens.produto"})
    List<Venda> findAll();

    @EntityGraph(attributePaths = {"funcionario", "caixa", "itens", "itens.produto"})
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Venda> findByCaixa(Caixa caixa);

    List<Venda> findByCaixaId(Long caixaId);
}
