package IJI.Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import IJI.Backend.Entity.Agente;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByUsername(String correo);
    List<Agente> findByNombreInmobiliaria(String nombreInmobiliaria);
}
