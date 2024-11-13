package IJI.Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import IJI.Backend.Entity.Propiedad;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    Optional<Propiedad> findById(Long id);
    List<Propiedad> findByIdAgente(Long idAgente);
    boolean existsByIdAgente(Long idAgente);
}
