package com.brunomessias.portfolio_java_fullstack.services;

import com.brunomessias.portfolio_java_fullstack.dto.projeto.AtualizarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.dto.projeto.CriarProjetoDTO;
import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import com.brunomessias.portfolio_java_fullstack.entities.StatusProjeto;
import com.brunomessias.portfolio_java_fullstack.repository.PessoaRepository;
import com.brunomessias.portfolio_java_fullstack.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final PessoaRepository pessoaRepository;

    public Projeto criar(CriarProjetoDTO dto) {
        Pessoa gerente = pessoaRepository.findById(dto.gerenteId())
                .orElseThrow(() -> new EntityNotFoundException("Gerente não encontrado"));

        if (dto.dataPrevisaoFim().isBefore(dto.dataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de previsão de fim não pode ser anterior à data de início");
        }

        Projeto p = Projeto.builder()
                .nome(dto.nome())
                .dataInicio(dto.dataInicio())
                .dataPrevisaoFim(dto.dataPrevisaoFim())
                .descricao(dto.descricao())
                .orcamento(dto.orcamento())
                .risco(dto.risco())
                .status(StatusProjeto.EM_ANALISE)
                .gerente(gerente)
                .build();

        if (dto.membrosIds() != null) {
            Set<Pessoa> membros = pessoaRepository.findAllByIdIn(dto.membrosIds());
            validarMembros(membros);
            p.setMembros(membros);
        }

        return projetoRepository.save(p);
    }

    public List<Projeto> listarTodos() {
        return projetoRepository.findAll();
    }

    public Projeto buscar(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));
    }

    @Transactional
    public Projeto atualizar(Long id, AtualizarProjetoDTO dto) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));

        if (dto.nome() != null) projeto.setNome(dto.nome());
        if (dto.dataInicio() != null) projeto.setDataInicio(dto.dataInicio());
        if (dto.dataPrevisaoFim() != null) projeto.setDataPrevisaoFim(dto.dataPrevisaoFim());
        if (dto.descricao() != null) projeto.setDescricao(dto.descricao());
        if (dto.orcamento() != null) projeto.setOrcamento(dto.orcamento());
        if (dto.risco() != null) projeto.setRisco(dto.risco());

        if (projeto.getDataPrevisaoFim().isBefore(projeto.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de previsão de fim não pode ser anterior à data de início");
        }

        if (dto.gerenteId() != null) {
            Pessoa novoGerente = pessoaRepository.findById(dto.gerenteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado"));
            projeto.setGerente(novoGerente);
        }

        if (dto.status() != null && dto.status().equals(StatusProjeto.ENCERRADO) && projeto.getDataFim() == null) {
            projeto.setDataFim(LocalDate.now());
        }

        if (dto.membrosIds() != null) {
            Set<Pessoa> membros = pessoaRepository.findAllByIdIn(dto.membrosIds());
            validarMembros(membros);
            projeto.setMembros(membros);
        }

        if (dto.status() != null) {
            projeto.setStatus(dto.status());
        }

        return projetoRepository.save(projeto);
    }

    public void excluir(Long id) {
        Projeto p = projetoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Projeto não encontrado"));

        if (List.of(StatusProjeto.INICIADO,
                StatusProjeto.EM_ANDAMENTO,
                StatusProjeto.ENCERRADO).contains(p.getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Projeto não pode ser excluído");
        }

        projetoRepository.delete(p);
    }

    private void validarMembros(Set<Pessoa> membros) {
        if (!membros.isEmpty()) {

            boolean temNaoFuncionario = membros.stream()
                    .anyMatch(pessoa -> !pessoa.getFuncionario());

            if (temNaoFuncionario) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Apenas funcionários podem ser membros de projetos"
                );
            }
        }
    }
}