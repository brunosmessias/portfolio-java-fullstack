package com.brunomessias.portfolio_java_fullstack.controller;

import com.brunomessias.portfolio_java_fullstack.dto.pessoa.CriarPessoaDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.services.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoa", description = "Operações relacionadas a pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    @Operation(
            summary = "Cria uma nova pessoa",
            description = "Cadastra uma nova pessoa no sistema. A atribuição deve ser FUNCIONARIO ou GERENTE."
    )
    public ResponseEntity<Pessoa> criar(@RequestBody CriarPessoaDTO dto) {
        Pessoa pessoa = pessoaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }
}