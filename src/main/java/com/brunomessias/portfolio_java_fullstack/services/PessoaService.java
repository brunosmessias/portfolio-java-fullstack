package com.brunomessias.portfolio_java_fullstack.services;

import com.brunomessias.portfolio_java_fullstack.dto.pessoa.CriarPessoaDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public Pessoa criar(CriarPessoaDTO dto) {
        String atribuicao = dto.atribuicao().trim().toUpperCase();

        if (!atribuicao.equals("FUNCIONARIO") && !atribuicao.equals("GERENTE")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Atribuição deve ser FUNCIONARIO ou GERENTE");
        }

        Pessoa pessoa = Pessoa.builder()
                .nome(dto.nome())
                .funcionario("FUNCIONARIO".equals(atribuicao))
                .gerente("GERENTE".equals(atribuicao))
                .build();

        return pessoaRepository.save(pessoa);
    }

    public List<Pessoa> buscarGerentes() {
        return pessoaRepository.findByGerenteTrue();
    }

    public List<Pessoa> buscarFuncionarios() {
        return pessoaRepository.findByFuncionarioTrue();
    }


}