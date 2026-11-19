package br.com.prioriza.repository;

import br.com.prioriza.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// O JpaRepository oferece save, findById, findAll e delete automaticamente.
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByOrderByNomeAsc();
}
