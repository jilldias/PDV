package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Caixa;
import com.pdv.model.CaixaStatus;
import com.pdv.model.Funcionario;
import com.pdv.model.Venda;
import com.pdv.repository.CaixaRepository;
import com.pdv.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final VendaRepository vendaRepository;
    private final FuncionarioService funcionarioService;

    public CaixaService(CaixaRepository caixaRepository,
                        VendaRepository vendaRepository,
                        FuncionarioService funcionarioService) {
        this.caixaRepository = caixaRepository;
        this.vendaRepository = vendaRepository;
        this.funcionarioService = funcionarioService;
    }

    @Transactional
    public Caixa abrirCaixa(Caixa caixa) {
        caixaRepository.findFirstByStatusOrderByDataAberturaDesc(CaixaStatus.ABERTO)
                .ifPresent(caixaAberto -> {
                    throw new IllegalStateException("Já existe um caixa aberto");
                });

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
        if (caixa.getStatus() == CaixaStatus.FECHADO) {
            throw new IllegalStateException("Caixa já está fechado");
        }

        caixa.setValorFinal(valorFinal);
        caixa.setDataFechamento(LocalDateTime.now());
        caixa.setStatus(CaixaStatus.FECHADO);
        return caixaRepository.save(caixa);
    }

    public List<Caixa> listarTodos() {
        return caixaRepository.findAll();
    }

    public Optional<Caixa> buscarCaixaAberto() {
        return caixaRepository.findFirstByStatusOrderByDataAberturaDesc(CaixaStatus.ABERTO);
    }

    public BigDecimal calcularTotalVendas(Caixa caixa) {
        if (caixa == null || caixa.getId() == null) {
            return BigDecimal.ZERO;
        }

        return vendaRepository.findByCaixaId(caixa.getId()).stream()
                .map(Venda::getValorTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Caixa buscarPorId(Long id) {
        return caixaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caixa", "id", id));
    }
}
