package noemicoppotelli.finalbuildweek.repositories;

import noemicoppotelli.finalbuildweek.entities.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, Long> {
}