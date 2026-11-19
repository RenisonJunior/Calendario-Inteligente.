package br.com.prioriza.repository;

    import br.com.prioriza.model.RegistroFoco;
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;

    // O JpaRepository oferece save, findById, findAll e delete automaticamente.
    public interface RegistroFocoRepository extends JpaRepository<RegistroFoco, Long> {
        List<RegistroFoco> findByTarefaIdOrderByIdAsc(Long tarefaId);
void deleteByTarefaId(Long tarefaId);
    }
