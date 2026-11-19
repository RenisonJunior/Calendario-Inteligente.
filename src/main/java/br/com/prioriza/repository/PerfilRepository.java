package br.com.prioriza.repository;

import br.com.prioriza.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// O JpaRepository oferece save, findById, findAll e delete automaticamente.
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

}
