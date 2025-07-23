package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import org.mockito.invocation.InvocationOnMock;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjetoTestDataBuilder {

    public static CriarProjetoDTO criarProjetoDTO() {
        return new CriarProjetoDTO(
                "Projeto Teste",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 6, 1),
                "Descrição do projeto",
                10000.0,
                RiscoProjeto.BAIXO,
                1L,
                List.of(2L, 3L)
        );
    }

    public static Projeto criarProjetoEmAnalise() {
        return Projeto.builder()
                .nome("Projeto Teste")
                .dataInicio(LocalDate.of(2025, 1, 1))
                .dataPrevisaoFim(LocalDate.of(2025, 12, 1))
                .descricao("Descrição do projeto")
                .orcamento(10000.0)
                .risco(RiscoProjeto.BAIXO)
                .status(StatusProjeto.EM_ANALISE)
                .gerente(criarGerente())
                .membros(new HashSet<>())
                .build();
    }

    public static CriarProjetoDTO criarProjetoDTOComDatasInvalidas() {
        return new CriarProjetoDTO(
                "Projeto Inválido",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 1, 1), // Data fim anterior
                "Descrição",
                1000.0,
                RiscoProjeto.ALTO,
                1L,
                null
        );
    }


    public static Pessoa criarGerente() {
        return Pessoa.builder()
                .nome("Gerente Teste")
                .gerente(true)
                .funcionario(false)
                .build();
    }

    public static Set<Pessoa> criarMembrosValidos() {
        return Set.of(
                criarFuncionario("Funcionário 1", 2L),
                criarFuncionario("Funcionário 2", 3L)
        );
    }

    public static Set<Pessoa> criarMembrosComGerentes() {
        return Set.of(
                criarFuncionario("Funcionário", 2L),
                Pessoa.builder()
                        .id(3L)
                        .nome("Gerente como Membro")
                        .gerente(true)
                        .funcionario(false)
                        .build()
        );
    }

    public static Projeto salvarComId(InvocationOnMock invocation) {
        Projeto projeto = invocation.getArgument(0);
        projeto.setId(1L);
        return projeto;
    }

    public static Pessoa criarGerente(String nome, Long id) {
        return Pessoa.builder()
                .id(id)
                .nome(nome)
                .gerente(true)
                .funcionario(false)
                .build();
    }

    public static Pessoa criarFuncionario(String nome) {
        return criarFuncionario(nome, null);
    }

    public static Pessoa criarFuncionario(String nome, Long id) {
        return Pessoa.builder()
                .id(id)
                .nome(nome)
                .gerente(false)
                .funcionario(true)
                .build();
    }

    public static Projeto criarProjetoCompleto() {
        return Projeto.builder()
                .id(1L)
                .nome("Projeto Completo")
                .dataInicio(LocalDate.of(2025, 1, 1))
                .dataPrevisaoFim(LocalDate.of(2025, 6, 1))
                .descricao("Descrição completa")
                .orcamento(15000.0)
                .status(StatusProjeto.EM_ANALISE)
                .risco(RiscoProjeto.BAIXO)
                .gerente(criarGerente())
                .membros(criarMembrosValidos())
                .build();
    }

    public static Projeto criarProjetoEmAndamento() {
        var projeto = criarProjetoEmAnalise();
        projeto.setStatus(StatusProjeto.EM_ANDAMENTO);
        return projeto;
    }

    public static Projeto criarProjetoEncerrado() {
        var projeto = criarProjetoEmAnalise();
        projeto.setStatus(StatusProjeto.ENCERRADO);
        projeto.setDataFim(LocalDate.now().minusDays(1));
        return projeto;
    }

    public static Projeto criarProjetoComMembros() {
        var projeto = criarProjetoEmAnalise();
        projeto.setMembros(criarMembrosValidos());
        return projeto;
    }
}