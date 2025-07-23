package com.brunomessias.portfolio_java_fullstack;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Projeto Service - Testes de Criação")
class ProjetoServiceCriacaoTest {

    @Mock private ProjetoRepository projetoRepository;
    @Mock private PessoaRepository pessoaRepository;
    @InjectMocks private ProjetoService projetoService;

    @Test
    @DisplayName("Deve criar projeto com sucesso quando dados válidos")
    void deveCriarProjetoComSucesso() {
        var dto = ProjetoTestDataBuilder.criarProjetoDTO();
        var gerente = ProjetoTestDataBuilder.criarGerente();
        var membros = ProjetoTestDataBuilder.criarMembrosValidos();

        when(pessoaRepository.findById(dto.gerenteId()))
                .thenReturn(Optional.of(gerente));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
                .thenReturn(membros);
        when(projetoRepository.save(any(Projeto.class)))
                .thenAnswer(ProjetoTestDataBuilder::salvarComId);

        var resultado = projetoService.criar(dto);

        assertThat(resultado)
                .satisfies(projeto -> {
                    assertThat(projeto.getId()).isNotNull();
                    assertThat(projeto.getNome()).isEqualTo(dto.nome());
                    assertThat(projeto.getStatus()).isEqualTo(StatusProjeto.EM_ANALISE);
                    assertThat(projeto.getGerente()).isEqualTo(gerente);
                    assertThat(projeto.getMembros()).hasSize(2);
                });

        verify(projetoRepository).save(any(Projeto.class));
    }

    @Test
    @DisplayName("Deve falhar quando gerente não existe")
    void deveFalharQuandoGerenteNaoExiste() {
        var dto = ProjetoTestDataBuilder.criarProjetoDTO();
        when(pessoaRepository.findById(dto.gerenteId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Gerente não encontrado");

        verify(projetoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar quando data fim anterior ao início")
    void deveFalharQuandoDataFimAnteriorInicio() {
        var dto = ProjetoTestDataBuilder.criarProjetoDTOComDatasInvalidas();
        var gerente = ProjetoTestDataBuilder.criarGerente();

        when(pessoaRepository.findById(dto.gerenteId()))
                .thenReturn(Optional.of(gerente));

        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Deve falhar quando membros não são funcionários")
    void deveFalharQuandoMembrosNaoSaoFuncionarios() {
        var dto = ProjetoTestDataBuilder.criarProjetoDTO();
        var gerente = ProjetoTestDataBuilder.criarGerente();
        var membrosInvalidos = ProjetoTestDataBuilder.criarMembrosComGerentes();

        when(pessoaRepository.findById(dto.gerenteId()))
                .thenReturn(Optional.of(gerente));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
                .thenReturn(membrosInvalidos);

        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}