package com.brunomessias.portfolio_java_fullstack.repository;

import com.brunomessias.portfolio_java_fullstack.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

}