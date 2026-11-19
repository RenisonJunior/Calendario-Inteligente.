package br.com.prioriza.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "registros_foco")
public class RegistroFoco {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private int minutos;

    // FK: registra o tempo gasto em cada tarefa.
    @ManyToOne(optional = false)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    public Long getId() { return id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public int getMinutos() { return minutos; }
    public void setMinutos(int minutos) { this.minutos = minutos; }
    public Tarefa getTarefa() { return tarefa; }
    public void setTarefa(Tarefa tarefa) { this.tarefa = tarefa; }
}
