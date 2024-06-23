package br.app.precojusto.repositories;

import br.app.precojusto.models.Pato;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PatoRepository
 */
public interface PatoRepository extends JpaRepository<Pato, Long> {
    List<Pato> findAllByVendaIsNull();
}
