package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Caixa;
import com.pdv.model.CaixaStatus;
import com.pdv.model.Funcionario;
import com.pdv.repository.CaixaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final FuncionarioService funcionarioService;

    public CaixaService(CaixaRepository caixaRepository, FuncionarioService funcionarioService) {
        this.caixaRepository = caixaRepository;
        this.funcionarioService = funcionarioService;
    }

    @Transactional
    public Caixa abrirCaixa(Caixa caixa) {
        Funcionario funcionario = funcionarioService.buscarPorId(caixa.getFuncionario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "id", caixa.getFuncionario().getId()));
        caixa.setFuncionario(funcionario);
        caixa.setStatus(CaixaStatus.ABERTO);
        caixa.setDataAbertura(LocalDateTime.now());
        return caixaRepository.save(caixa);
    }

    @Transactional
    public Caixa fecharCaixa(Long id, BigDecimal valorFinal) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caixa", "id", id));
        caixa.setValorFinal(valorFinal);
        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setStatus(CaixaStatus.FECHADO);
        return caixaRepository.save(caixa);
    }

    public List<Caixa> listarTodos() {
        return caixaRepository.findAll();
    }

    public Caixa buscarPorId(Long id) {
        return caixaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caixa", "id", id));
    }
}
