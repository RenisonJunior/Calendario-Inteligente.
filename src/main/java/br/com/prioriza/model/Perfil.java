package br.com.prioriza.model;

import jakarta.persistence.*;

// Perfil local único: projeto didático sem login para facilitar a apresentação.
@Entity
@Table(name = "perfis")
public class Perfil {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String nome;
    private int minutosDisponiveis = 120;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getMinutosDisponiveis() { return minutosDisponiveis; }
    public void setMinutosDisponiveis(int minutosDisponiveis) { this.minutosDisponiveis = minutosDisponiveis; }
}
