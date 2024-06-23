package br.app.precojusto.repositories;

import br.app.precojusto.models.Cliente;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ClienteRepository
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);
    List<Cliente> findByAtivoTrue();
    Page<Cliente> findByAtivoTrue(Pageable pageable);
}
