package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Projeto Service - Testes de Exclusão")
class ProjetoServiceExclusaoTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @ParameterizedTest
    @EnumSource(value = StatusProjeto.class, 
               names = {"INICIADO", "EM_ANDAMENTO", "ENCERRADO"})
    @DisplayName("Deve impedir exclusão de projetos com status restritivos")
    void deveImpedirExclusaoComStatusRestritivos(StatusProjeto statusRestritivo) {
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setStatus(statusRestritivo);
        
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));

        assertThatThrownBy(() -> projetoService.excluir(1L))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
                assertThat(responseException.getReason())
                    .contains("Projeto não pode ser excluído");
            });

        verify(projetoRepository, never()).delete(any(Projeto.class));
    }

    @ParameterizedTest
    @EnumSource(value = StatusProjeto.class,
               names = {"INICIADO", "EM_ANDAMENTO", "ENCERRADO"},
               mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("Deve permitir exclusão de projetos com status permitidos")
    void devePermitirExclusaoComStatusPermitidos(StatusProjeto statusPermitido) {
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setStatus(statusPermitido);
        
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));

        projetoService.excluir(1L);

        verify(projetoRepository).delete(projeto);
    }

    @Test
    @DisplayName("Deve falhar ao tentar excluir projeto inexistente")
    void deveFalharAoExcluirProjetoInexistente() {
        var idInexistente = 999L;
        when(projetoRepository.findById(idInexistente))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> projetoService.excluir(idInexistente))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(responseException.getReason()).contains("Projeto não encontrado");
            });

        verify(projetoRepository, never()).delete(any(Projeto.class));
    }

    @Test
    @DisplayName("Deve validar se projeto foi realmente excluído")
    void deveValidarSeProjetoFoiExcluido() {
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));

        projetoService.excluir(1L);

        verify(projetoRepository, times(1)).findById(1L);
        verify(projetoRepository, times(1)).delete(projeto);
        verifyNoMoreInteractions(projetoRepository);
    }

    @Test
    @DisplayName("Deve excluir projeto em análise mesmo com membros associados")
    void deveExcluirProjetoComMembrosAssociados() {
        var projetoComMembros = ProjetoTestDataBuilder.criarProjetoComMembros();
        projetoComMembros.setStatus(StatusProjeto.EM_ANALISE);
        
        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoComMembros));

        projetoService.excluir(1L);

        verify(projetoRepository).delete(projetoComMembros);
    }
}