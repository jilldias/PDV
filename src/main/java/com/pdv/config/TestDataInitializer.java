package com.pdv.config;

import com.pdv.model.Categoria;
import com.pdv.model.Funcionario;
import com.pdv.repository.CategoriaRepository;
import com.pdv.repository.FuncionarioRepository;
import com.pdv.security.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer implements ApplicationRunner {

    private final FuncionarioRepository funcionarioRepository;
    private final CategoriaRepository categoriaRepository;

    public TestDataInitializer(
            FuncionarioRepository funcionarioRepository,
            CategoriaRepository categoriaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        criarAdminTeste();
        criarCategoriaSeNaoExistir("Geral", "Categoria padrao para testes");
        criarCategoriaSeNaoExistir("Bebidas", "Bebidas em geral");
        criarCategoriaSeNaoExistir("Alimentos", "Produtos alimenticios");
    }

    private void criarAdminTeste() {
        if (funcionarioRepository.findByLogin("admin").isPresent()) {
            return;
        }

        Funcionario admin = new Funcionario();
        admin.setNome("Administrador Teste");
        admin.setCargo("Administrador");
        admin.setEmail("admin@pdv.local");
        admin.setTelefone("");
        admin.setLogin("admin");
        admin.setSenha("admin");
        admin.setRole(Role.ADMIN);

        funcionarioRepository.save(admin);
    }

    private void criarCategoriaSeNaoExistir(String nome, String descricao) {
        if (categoriaRepository.findByNomeIgnoreCase(nome).isPresent()) {
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setDescricao(descricao);

        categoriaRepository.save(categoria);
    }
}
