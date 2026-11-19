package br.com.prioriza.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String titulo;
    @Column(length = 800)
    private String descricao = "";
    @Column(nullable = false)
    private LocalDate prazo;
    @Column(nullable = false)
    private int minutosEstimados = 30;
    @Column(nullable = false)
    private boolean concluida = false;

    // FK: muitas tarefas podem pertencer a uma categoria.
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }
    public int getMinutosEstimados() { return minutosEstimados; }
    public void setMinutosEstimados(int minutosEstimados) { this.minutosEstimados = minutosEstimados; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
