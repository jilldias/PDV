package com.pdv.config;

import com.pdv.model.Categoria;
import com.pdv.model.Funcionario;
import com.pdv.repository.CategoriaRepository;
import com.pdv.repository.FuncionarioRepository;
import com.pdv.security.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seed-test-data", havingValue = "true")
public class TestDataInitializer implements ApplicationRunner {

    private final FuncionarioRepository funcionarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataInitializer(
            FuncionarioRepository funcionarioRepository,
            CategoriaRepository categoriaRepository,
            PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        criarAdminTeste();
        criarCategoriaSeNaoExistir("Geral", "Categoria padrao para testes");
        criarCategoriaSeNaoExistir("Bebidas", "Bebidas em geral");
        criarCategoriaSeNaoExistir("Alimentos", "Produtos alimenticios");
    }

    private void criarAdminTeste() {
        var adminExistente = funcionarioRepository.findByLogin("admin");
        if (adminExistente.isPresent()) {
            Funcionario admin = adminExistente.get();
            admin.setNome(admin.getNome() == null || admin.getNome().isBlank() ? "Administrador Teste" : admin.getNome());
            admin.setCargo(admin.getCargo() == null || admin.getCargo().isBlank() ? "Administrador" : admin.getCargo());
            admin.setEmail(admin.getEmail() == null || admin.getEmail().isBlank() ? "admin@pdv.local" : admin.getEmail());
            admin.setTelefone(admin.getTelefone() == null ? "" : admin.getTelefone());
            admin.setRole(admin.getRole() == null ? Role.ADMIN : admin.getRole());
            admin.setSenha(passwordEncoder.encode("admin"));
            funcionarioRepository.save(admin);
            return;
        }

        Funcionario admin = new Funcionario();
        admin.setNome("Administrador Teste");
        admin.setCargo("Administrador");
        admin.setEmail("admin@pdv.local");
        admin.setTelefone("");
        admin.setLogin("admin");
        admin.setSenha(passwordEncoder.encode("admin"));
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
