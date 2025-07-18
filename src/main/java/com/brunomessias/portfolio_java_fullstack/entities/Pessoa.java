package com.brunomessias.portfolio_java_fullstack.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "datanascimento")
    private LocalDate dataNascimento;

    @Column(length = 14)
    private String cpf;

    private Boolean funcionario = false;

    private Boolean gerente = false;
}
