package com.brunomessias.portfolio_java_fullstack.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projeto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_previsao_fim")
    private LocalDate dataPrevisaoFim;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(length = 5000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    private StatusProjeto status;

    private Double orcamento;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    private RiscoProjeto risco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idgerente", nullable = false)
    private Pessoa gerente;

    @ManyToMany
    @JoinTable(
            name = "membros",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "pessoa_id")
    )
    private Set<Pessoa> membros = new HashSet<>();
}