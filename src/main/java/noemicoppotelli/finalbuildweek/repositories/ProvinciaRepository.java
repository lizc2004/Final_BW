package noemicoppotelli.finalbuildweek.repositories;

import noemicoppotelli.finalbuildweek.entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    Optional<Provincia> findByName(String name);

    Optional<Provincia> findBySigla(String sigla);

    boolean existsByName(String name);

    boolean existsBySigla(String sigla);

}
