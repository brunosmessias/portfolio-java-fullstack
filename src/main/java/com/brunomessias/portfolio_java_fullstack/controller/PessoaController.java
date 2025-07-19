package com.brunomessias.portfolio_java_fullstack.controller;

import com.brunomessias.portfolio_java_fullstack.dto.pessoa.CriarPessoaDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.services.PessoaService;
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
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<Pessoa> criar(@RequestBody CriarPessoaDTO dto) {
        Pessoa pessoa = pessoaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }
}