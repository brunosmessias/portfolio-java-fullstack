package com.brunomessias.portfolio_java_fullstack.dto.projeto;

import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CriarProjetoDTO(
        @NotBlank String nome,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataPrevisaoFim,
        String descricao,
        @NotNull Double orcamento,
        @NotNull RiscoProjeto risco,
        @NotNull Long gerenteId,
        List<Long> membrosIds
) {
}