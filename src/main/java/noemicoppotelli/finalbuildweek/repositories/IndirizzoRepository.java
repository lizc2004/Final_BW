package noemicoppotelli.finalbuildweek.repositories;

import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndirizzoRepository extends JpaRepository<Indirizzo, Long> {
    List<Indirizzo> findByClienteId(Long clienteId);

    Optional<Indirizzo> findByClienteIdAndTipoIndirizzo(
            Long clienteId,
            TipoIndirizzo tipoIndirizzo
    );

    boolean existsByClienteIdAndTipoIndirizzo(
            Long clienteId,
            TipoIndirizzo tipoIndirizzo
    );
}
