package noemicoppotelli.finalbuildweek.repositories;

import noemicoppotelli.finalbuildweek.entities.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, Long> {

    Optional<Comune> findByNome(String name);

    boolean existsByCodiceProvinciaAndProgressivoComune(
            String codiceProvincia,
            String progressivoComune
    );

    boolean existsBy();
}