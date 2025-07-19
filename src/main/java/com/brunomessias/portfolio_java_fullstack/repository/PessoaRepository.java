package com.brunomessias.portfolio_java_fullstack.repository;

import com.brunomessias.portfolio_java_fullstack.entities.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Set<Pessoa> findAllByIdIn(Collection<Long> id);

    List<Pessoa> findByGerenteTrue();

    List<Pessoa> findByFuncionarioTrue();
}