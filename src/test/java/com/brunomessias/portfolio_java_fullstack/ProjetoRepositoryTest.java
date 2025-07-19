package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true"
})
class ProjetoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Test
    @DisplayName("Deve salvar e recuperar projeto corretamente")
    void deveSalvarERecuperarProjetoCorretamente() {
        // Given
        Pessoa gerente = ProjetoTestDataBuilder.criarGerente();
        entityManager.persistAndFlush(gerente);

        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setGerente(gerente);

        // When
        Projeto projetoSalvo = projetoRepository.save(projeto);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Projeto> projetoRecuperado = projetoRepository.findById(projetoSalvo.getId());

        assertThat(projetoRecuperado).isPresent();
        assertThat(projetoRecuperado.get().getNome()).isEqualTo("Projeto Teste");
        assertThat(projetoRecuperado.get().getStatus()).isEqualTo(StatusProjeto.EM_ANALISE);
    }

    @Test
    @DisplayName("Deve manter relacionamento com membros")
    void deveManterRelacionamentoComMembros() {
        // Given
        Pessoa gerente = ProjetoTestDataBuilder.criarGerente();
        Pessoa membro1 = ProjetoTestDataBuilder.criarFuncionario("Membro 1");
        Pessoa membro2 = ProjetoTestDataBuilder.criarFuncionario("Membro 2");

        entityManager.persist(gerente);
        entityManager.persist(membro1);
        entityManager.persist(membro2);
        entityManager.flush();

        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setGerente(gerente);
        projeto.getMembros().add(membro1);
        projeto.getMembros().add(membro2);

        // When
        Projeto projetoSalvo = projetoRepository.save(projeto);
        entityManager.flush();
        entityManager.clear();

        // Then
        Projeto projetoRecuperado = projetoRepository.findById(projetoSalvo.getId()).get();
        assertThat(projetoRecuperado.getMembros()).hasSize(2);
    }
}