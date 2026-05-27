package com.pdv.repository;

import com.pdv.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Long> {
}
