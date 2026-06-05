package com.pdv.repository;

import com.pdv.model.Caixa;
import com.pdv.model.CaixaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    Optional<Caixa> findFirstByStatusOrderByDataAberturaDesc(CaixaStatus status);
}
