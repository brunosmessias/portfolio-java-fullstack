package com.brunomessias.portfolio_java_fullstack.repository;

import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    List<Pessoa> findByGerenteTrue();

    List<Pessoa> findByFuncionarioTrue();
}