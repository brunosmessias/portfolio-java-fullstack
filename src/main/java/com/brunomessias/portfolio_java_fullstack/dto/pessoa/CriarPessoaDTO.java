package com.brunomessias.portfolio_java_fullstack.dto.pessoa;

import jakarta.validation.constraints.NotBlank;

public record CriarPessoaDTO(
        @NotBlank String nome,
        @NotBlank String atribuicao
) {
}