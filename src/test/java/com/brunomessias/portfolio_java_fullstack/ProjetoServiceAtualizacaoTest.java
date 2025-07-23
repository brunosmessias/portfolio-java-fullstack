package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.AtualizarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Projeto Service - Testes de Atualização")
class ProjetoServiceAtualizacaoTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoService projetoService;

    @Test
    @DisplayName("Deve atualizar projeto com todos os campos informados")
    void deveAtualizarProjetoComTodosCampos() {
        // Given
        var projetoExistente = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        var novoGerente = ProjetoTestDataBuilder.criarGerente("Novo Gerente", 2L);
        var novosMembros = Set.of(
            ProjetoTestDataBuilder.criarFuncionario("Funcionário A", 3L),
            ProjetoTestDataBuilder.criarFuncionario("Funcionário B", 4L)
        );

        var dto = new AtualizarProjetoDTO(
            "Nome Atualizado",
            LocalDate.of(2025, 2, 1),
            LocalDate.of(2025, 8, 1),
            "Nova descrição detalhada",
            25000.0,
            StatusProjeto.EM_ANDAMENTO,
            RiscoProjeto.MEDIO,
            2L,
            List.of(3L, 4L)
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoExistente));
        when(pessoaRepository.findById(2L))
            .thenReturn(Optional.of(novoGerente));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
            .thenReturn(novosMembros);
        when(projetoRepository.save(any(Projeto.class)))
            .thenAnswer(i -> i.getArgument(0));

        // When
        var resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado)
            .satisfies(projeto -> {
                assertThat(projeto.getNome()).isEqualTo("Nome Atualizado");
                assertThat(projeto.getDataInicio()).isEqualTo(LocalDate.of(2025, 2, 1));
                assertThat(projeto.getDataPrevisaoFim()).isEqualTo(LocalDate.of(2025, 8, 1));
                assertThat(projeto.getDescricao()).isEqualTo("Nova descrição detalhada");
                assertThat(projeto.getOrcamento()).isEqualTo(25000.0);
                assertThat(projeto.getStatus()).isEqualTo(StatusProjeto.EM_ANDAMENTO);
                assertThat(projeto.getRisco()).isEqualTo(RiscoProjeto.MEDIO);
                assertThat(projeto.getGerente()).isEqualTo(novoGerente);
                assertThat(projeto.getMembros()).hasSize(2);
            });
    }

    @Test
    @DisplayName("Deve atualizar apenas campos informados mantendo os demais")
    void deveAtualizarApenasCamposInformados() {
        // Given
        var projetoExistente = ProjetoTestDataBuilder.criarProjetoCompleto();
        var nomeOriginal = projetoExistente.getNome();
        var orcamentoOriginal = projetoExistente.getOrcamento();

        var dto = new AtualizarProjetoDTO(
            "Apenas Nome Novo", // apenas nome
            null, null, null, null, null, null, null, null
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projetoExistente));
        when(projetoRepository.save(any(Projeto.class)))
            .thenAnswer(i -> i.getArgument(0));

        // When
        var resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado)
            .satisfies(projeto -> {
                assertThat(projeto.getNome()).isEqualTo("Apenas Nome Novo");
                assertThat(projeto.getOrcamento()).isEqualTo(orcamentoOriginal);
                assertThat(projeto.getDataInicio()).isEqualTo(projetoExistente.getDataInicio());
                assertThat(projeto.getDescricao()).isEqualTo(projetoExistente.getDescricao());
            });
    }

    @Test
    @DisplayName("Deve definir data fim automaticamente quando status for ENCERRADO")
    void deveDefinirDataFimAutomaticamenteQuandoEncerrado() {
        // Given
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAndamento();
        var dto = new AtualizarProjetoDTO(
            null, null, null, null, null,
            StatusProjeto.ENCERRADO,
            null, null, null
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class)))
            .thenAnswer(i -> i.getArgument(0));

        // When
        var resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado)
            .satisfies(proj -> {
                assertThat(proj.getStatus()).isEqualTo(StatusProjeto.ENCERRADO);
                assertThat(proj.getDataFim()).isEqualTo(LocalDate.now());
            });
    }

    @Test
    @DisplayName("Não deve sobrescrever data fim se já estiver definida")
    void naoDeveSobrescreverDataFimExistente() {
        // Given
        var dataFimOriginal = LocalDate.of(2025, 1, 15);
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAndamento();
        projeto.setDataFim(dataFimOriginal);

        var dto = new AtualizarProjetoDTO(
            null, null, null, null, null,
            StatusProjeto.ENCERRADO,
            null, null, null
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any(Projeto.class)))
            .thenAnswer(i -> i.getArgument(0));

        // When
        var resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado.getDataFim())
            .isEqualTo(dataFimOriginal);
    }

    @Test
    @DisplayName("Deve falhar quando projeto não encontrado para atualização")
    void deveFalharQuandoProjetoNaoEncontrado() {
        // Given
        var dto = new AtualizarProjetoDTO(
            "Nome", null, null, null, null, null, null, null, null
        );
        
        when(projetoRepository.findById(999L))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projetoService.atualizar(999L, dto))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            });
    }

    @Test
    @DisplayName("Deve falhar quando novo gerente não encontrado")
    void deveFalharQuandoNovoGerenteNaoEncontrado() {
        // Given
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        var dto = new AtualizarProjetoDTO(
            null, null, null, null, null, null, null,
            999L, // gerente inexistente
            null
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));
        when(pessoaRepository.findById(999L))
            .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> projetoService.atualizar(1L, dto))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(responseException.getReason()).contains("Gerente não encontrado");
            });
    }

    @Test
    @DisplayName("Deve falhar quando membros não são funcionários")
    void deveFalharQuandoMembrosNaoSaoFuncionarios() {
        // Given
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        var dto = new AtualizarProjetoDTO(
            null, null, null, null, null, null, null, null,
            List.of(2L, 3L)
        );

        var funcionario = ProjetoTestDataBuilder.criarFuncionario("Funcionário", 2L);
        var gerenteComoMembro = Pessoa.builder()
            .id(3L)
            .nome("Gerente Inválido")
            .gerente(true)
            .funcionario(false)
            .build();

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
            .thenReturn(Set.of(funcionario, gerenteComoMembro));

        // When & Then
        assertThatThrownBy(() -> projetoService.atualizar(1L, dto))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(responseException.getReason())
                    .contains("Apenas funcionários podem ser membros de projetos");
            });
    }

    @Test
    @DisplayName("Deve falhar quando data fim anterior ao início na atualização")
    void deveFalharQuandoDataFimAnteriorInicio() {
        // Given
        var projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        var dto = new AtualizarProjetoDTO(
            null,
            LocalDate.of(2025, 6, 1), // início
            LocalDate.of(2025, 3, 1), // fim anterior
            null, null, null, null, null, null
        );

        when(projetoRepository.findById(1L))
            .thenReturn(Optional.of(projeto));

        // When & Then
        assertThatThrownBy(() -> projetoService.atualizar(1L, dto))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(exception -> {
                var responseException = (ResponseStatusException) exception;
                assertThat(responseException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(responseException.getReason())
                    .contains("Data de previsão de fim não pode ser anterior à data de início");
            });
    }
}