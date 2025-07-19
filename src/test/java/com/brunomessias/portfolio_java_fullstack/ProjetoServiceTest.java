package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.AtualizarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import com.brunomessias.portfolio_java_fullstack.services.ProjetoService;
import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoService projetoService;

    // ===== TESTES DE CRIAÇÃO =====

    @Test
    @DisplayName("Deve criar projeto com sucesso")
    void deveCriarProjetoComSucesso() {
        // Given
        CriarProjetoDTO dto = ProjetoTestDataBuilder.criarProjetoDTO();
        Pessoa gerente = ProjetoTestDataBuilder.criarGerente();
        Set<Pessoa> membros = Set.of(
                ProjetoTestDataBuilder.criarFuncionario("Funcionário 1"),
                ProjetoTestDataBuilder.criarFuncionario("Funcionário 2")
        );

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds())).thenReturn(membros);
        when(projetoRepository.save(any(Projeto.class))).thenAnswer(i -> {
            Projeto p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        // When
        Projeto resultado = projetoService.criar(dto);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo(dto.nome());
        assertThat(resultado.getStatus()).isEqualTo(StatusProjeto.EM_ANALISE);
        assertThat(resultado.getGerente()).isEqualTo(gerente);
        assertThat(resultado.getMembros()).hasSize(2);

        verify(projetoRepository).save(any(Projeto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando gerente não encontrado")
    void deveLancarExcecaoQuandoGerenteNaoEncontrado() {
        // Given
        CriarProjetoDTO dto = ProjetoTestDataBuilder.criarProjetoDTO();
        when(pessoaRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Gerente não encontrado");

        verify(projetoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando data previsão fim anterior ao início")
    void deveLancarExcecaoQuandoDataPrevisaoFimAnteriorInicio() {
        // Given
        CriarProjetoDTO dto = new CriarProjetoDTO(
                "Projeto Inválido",
                LocalDate.of(2025, 6, 1), // Data início
                LocalDate.of(2025, 1, 1), // Data fim anterior
                "Descrição",
                1000.0,
                RiscoProjeto.ALTO,
                1L,
                null
        );

        Pessoa gerente = ProjetoTestDataBuilder.criarGerente();
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));

        // When/Then
        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining("Data de previsão de fim não pode ser anterior à data de início");
    }

    // ===== TESTES DE BUSCA =====

    @Test
    @DisplayName("Deve buscar projeto por ID com sucesso")
    void deveBuscarProjetoPorIdComSucesso() {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // When
        Projeto resultado = projetoService.buscar(1L);

        // Then
        assertThat(resultado).isEqualTo(projeto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando projeto não encontrado")
    void deveLancarExcecaoQuandoProjetoNaoEncontrado() {
        // Given
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> projetoService.buscar(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND)
                .hasMessageContaining("Projeto não encontrado");
    }

    // ===== TESTES DE EXCLUSÃO =====

    @Test
    @DisplayName("Deve excluir projeto em análise com sucesso")
    void deveExcluirProjetoEmAnaliseComSucesso() {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // When
        projetoService.excluir(1L);

        // Then
        verify(projetoRepository).delete(projeto);
    }

    @ParameterizedTest
    @EnumSource(value = StatusProjeto.class, names = {"INICIADO", "EM_ANDAMENTO", "ENCERRADO"})
    @DisplayName("Deve impedir exclusão de projetos com status restritivos")
    void deveImpedirExclusaoProjetosComStatusRestritivos(StatusProjeto status) {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setStatus(status);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // When/Then
        assertThatThrownBy(() -> projetoService.excluir(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.UNPROCESSABLE_ENTITY)
                .hasMessageContaining("Projeto não pode ser excluído");

        verify(projetoRepository, never()).delete(any());
    }

    @ParameterizedTest
    @EnumSource(value = StatusProjeto.class,
            names = {"INICIADO", "EM_ANDAMENTO", "ENCERRADO"},
            mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("Deve permitir exclusão de projetos com status permitidos")
    void devePermitirExclusaoProjetosComStatusPermitidos(StatusProjeto status) {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setStatus(status);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

        // When
        projetoService.excluir(1L);

        // Then
        verify(projetoRepository).delete(projeto);
    }

    // ===== TESTES DE ATUALIZAÇÃO =====

    @Test
    @DisplayName("Deve atualizar projeto com sucesso")
    void deveAtualizarProjetoComSucesso() {
        // Given
        Projeto projetoExistente = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        AtualizarProjetoDTO dto = new AtualizarProjetoDTO(
                "Nome Atualizado",
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 7, 1),
                "Nova descrição",
                20000.0,
                StatusProjeto.INICIADO,
                RiscoProjeto.ALTO,
                1L, // mesmo gerente
                List.of(2L)
        );

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projetoExistente));
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(projetoExistente.getGerente()));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
                .thenReturn(Set.of(ProjetoTestDataBuilder.criarFuncionario("Funcionário")));
        when(projetoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // When
        Projeto resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado.getNome()).isEqualTo("Nome Atualizado");
        assertThat(resultado.getStatus()).isEqualTo(StatusProjeto.INICIADO);
        assertThat(resultado.getOrcamento()).isEqualTo(20000.0);
        assertThat(resultado.getRisco()).isEqualTo(RiscoProjeto.ALTO);
    }

    @Test
    @DisplayName("Deve definir data fim automaticamente quando status for ENCERRADO")
    void deveDefinirDataFimAutomaticamenteQuandoStatusEncerrado() {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        AtualizarProjetoDTO dto = new AtualizarProjetoDTO(
                null, null, null, null, null,
                StatusProjeto.ENCERRADO, // Mudando para encerrado
                null, null, null
        );

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // When
        Projeto resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado.getStatus()).isEqualTo(StatusProjeto.ENCERRADO);
        assertThat(resultado.getDataFim()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Deve permitir apenas funcionários como membros do projeto")
    void devePermitirApenasFuncionariosComoMembros() {
        // Given
        CriarProjetoDTO dto = ProjetoTestDataBuilder.criarProjetoDTO();
        Pessoa gerente = ProjetoTestDataBuilder.criarGerente();

        // Criando pessoas com atribuições diferentes
        Pessoa funcionario = ProjetoTestDataBuilder.criarFuncionario("Funcionário");
        Pessoa gerenteComoMembro = Pessoa.builder()
                .nome("Outro Gerente")
                .gerente(true)
                .funcionario(false)
                .build();

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(gerente));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
                .thenReturn(Set.of(funcionario, gerenteComoMembro));

        // When/Then
        assertThatThrownBy(() -> projetoService.criar(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining("Apenas funcionários podem ser membros de projetos");

        verify(projetoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve aceitar apenas funcionários na atualização de membros")
    void deveAceitarApenasFuncionariosNaAtualizacaoMembros() {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        AtualizarProjetoDTO dto = new AtualizarProjetoDTO(
                null, null, null, null, null, null, null, null,
                List.of(2L, 3L) // IDs dos novos membros
        );

        Pessoa funcionario = ProjetoTestDataBuilder.criarFuncionario("Funcionário");
        Pessoa gerente = Pessoa.builder()
                .id(3L)
                .nome("Gerente como Membro")
                .gerente(true)
                .funcionario(false)
                .build();

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(pessoaRepository.findAllByIdIn(dto.membrosIds()))
                .thenReturn(Set.of(funcionario, gerente));

        // When/Then
        assertThatThrownBy(() -> projetoService.atualizar(1L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining("Apenas funcionários podem ser membros de projetos");
    }

    @Test
    @DisplayName("Não deve sobrescrever data fim se já estiver definida")
    void naoDeveSobrescreverDataFimSeJaEstiverDefinida() {
        // Given
        Projeto projeto = ProjetoTestDataBuilder.criarProjetoEmAnalise();
        projeto.setDataFim(LocalDate.of(2025, 1, 10)); // Já tem data fim
        projeto.setStatus(StatusProjeto.EM_ANDAMENTO);

        AtualizarProjetoDTO dto = new AtualizarProjetoDTO(
                null, null, null, null, null,
                StatusProjeto.ENCERRADO, // Mudando para encerrado
                null, null, null
        );

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // When
        Projeto resultado = projetoService.atualizar(1L, dto);

        // Then
        assertThat(resultado.getDataFim()).isEqualTo(LocalDate.of(2025, 1, 10)); // Mantém a data original
    }
}