package br.com.prioriza.repository;

    import br.com.prioriza.model.Tarefa;
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;

    // O JpaRepository oferece save, findById, findAll e delete automaticamente.
    public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
        List<Tarefa> findAllByOrderByPrazoAsc();
boolean existsByCategoriaId(Long categoriaId);
    }
