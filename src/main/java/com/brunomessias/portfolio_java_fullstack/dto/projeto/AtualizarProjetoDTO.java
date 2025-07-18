package com.brunomessias.portfolio_java_fullstack.dto.projeto;

import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;

import java.time.LocalDate;
import java.util.List;

public record AtualizarProjetoDTO(
        String nome,
        LocalDate dataInicio,
        LocalDate dataPrevisaoFim,
        String descricao,
        Double orcamento,
        StatusProjeto status,
        RiscoProjeto risco,
        Long gerenteId,
        List<Long> membrosIds
) {
}