package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Projeto Service - Testes de Busca")
class ProjetoServiceBuscaTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @Test
    @DisplayName("Deve buscar projeto por ID com sucesso quando projeto existe")
    void deveBuscarProjetoPorIdComSucesso() {
        // Given
        var projetoEsperado = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoEsperado));

        // When
        var resultado = projetoService.buscar(1L);

        // Then
        assertThat(resultado)
            .isNotNull()
            .satisfies(projeto -> {
                assertThat(projeto.getId()).isEqualTo(projetoEsperado.getId());
                assertThat(projeto.getNome()).isEqualTo(projetoEsperado.getNome());
                assertThat(projeto.getStatus()).isEqualTo(projetoEsperado.getStatus());
                assertThat(projeto.getGerente()).isEqualTo(projetoEsperado.getGerente());
            });
    }

    @Test
    @DisplayName("Deve lançar exceção quando projeto não encontrado")
    void deveLancarExcecaoQuandoProjetoNaoEncontrado() {
        // Given
        var idInexistente = 999L;
        when(projetoRepository.findById(idInexistente))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projetoService.buscar(idInexistente))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(responseException.getReason()).contains("Projeto não encontrado");
            });
    }

    @Test
    @DisplayName("Deve buscar projeto com todos os relacionamentos carregados")
    void deveBuscarProjetoComRelacionamentosCarregados() {
        // Given
        var projetoCompleto = ProjetoTestDataBuilder.criarProjetoComMembros();
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoCompleto));

        // When
        var resultado = projetoService.buscar(1L);

        // Then
        assertThat(resultado)
            .satisfies(projeto -> {
                assertThat(projeto.getGerente()).isNotNull();
                assertThat(projeto.getMembros()).isNotEmpty();
                assertThat(projeto.getMembros()).hasSize(2);
            });
    }

    @Test
    @DisplayName("Deve buscar projeto independente do status")
    void deveBuscarProjetoIndependenteDoStatus() {
        // Given
        var projetoEncerrado = ProjetoTestDataBuilder.criarProjetoEncerrado();
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoEncerrado));

        // When
        var resultado = projetoService.buscar(1L);

        // Then
        assertThat(resultado)
            .isNotNull()
            .extracting(Projeto::getStatus)
            .isEqualTo(projetoEncerrado.getStatus());
    }
}