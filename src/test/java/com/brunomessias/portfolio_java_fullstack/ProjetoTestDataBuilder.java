package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class ProjetoTestDataBuilder {

    public static Pessoa criarGerente() {
        return Pessoa.builder()
                .nome("João Gerente")
                .gerente(true)
                .funcionario(false)
                .build();
    }

    public static Pessoa criarFuncionario(String nome) {
        return Pessoa.builder()
                .nome(nome)
                .funcionario(true)
                .gerente(false)
                .build();
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

    public static CriarProjetoDTO criarProjetoDTO() {
        return new CriarProjetoDTO(
                "Novo Projeto",
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 8, 1),
                "Descrição",
                15000.0,
                RiscoProjeto.MEDIO,
                1L,
                List.of(2L, 3L)
        );
    }
}