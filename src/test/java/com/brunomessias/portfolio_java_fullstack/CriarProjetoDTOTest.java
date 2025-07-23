package com.brunomessias.portfolio_java_fullstack;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.RiscoProjeto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CriarProjetoDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar DTO válido sem erros")
    void deveValidarDTOValidoSemErros() {
        CriarProjetoDTO dto = ProjetoTestDataBuilder.criarProjetoDTO();

        Set<ConstraintViolation<CriarProjetoDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar quando nome for nulo ou vazio")
    void deveFalharQuandoNomeForNuloOuVazio() {
        CriarProjetoDTO dtoNulo = new CriarProjetoDTO(
                null, LocalDate.now(), LocalDate.now().plusDays(30),
                "desc", 1000.0, RiscoProjeto.BAIXO, 1L, null
        );

        CriarProjetoDTO dtoVazio = new CriarProjetoDTO(
                "", LocalDate.now(), LocalDate.now().plusDays(30),
                "desc", 1000.0, RiscoProjeto.BAIXO, 1L, null
        );

        Set<ConstraintViolation<CriarProjetoDTO>> violationsNulo = validator.validate(dtoNulo);
        Set<ConstraintViolation<CriarProjetoDTO>> violationsVazio = validator.validate(dtoVazio);

        assertThat(violationsNulo).hasSize(1);
        assertThat(violationsVazio).hasSize(1);
        assertThat(violationsNulo.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Deve falhar quando campos obrigatórios forem nulos")
    void deveFalharQuandoCamposObrigatoriosForemNulos(Object valor) {
        assertThat(validator.validate(new CriarProjetoDTO(
                "nome", (LocalDate) valor, LocalDate.now().plusDays(30),
                "desc", 1000.0, RiscoProjeto.BAIXO, 1L, null
        ))).isNotEmpty();

        assertThat(validator.validate(new CriarProjetoDTO(
                "nome", LocalDate.now(), (LocalDate) valor,
                "desc", 1000.0, RiscoProjeto.BAIXO, 1L, null
        ))).isNotEmpty();

        assertThat(validator.validate(new CriarProjetoDTO(
                "nome", LocalDate.now(), LocalDate.now().plusDays(30),
                "desc", (Double) valor, RiscoProjeto.BAIXO, 1L, null
        ))).isNotEmpty();
    }
}