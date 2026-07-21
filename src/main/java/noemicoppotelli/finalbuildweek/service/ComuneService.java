package noemicoppotelli.finalbuildweek.service;


import lombok.extern.slf4j.Slf4j;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ComuneService {
    private final ComuneRepository comuneRepository;

    public ComuneService(ComuneRepository comuneRepository) {
        this.comuneRepository = comuneRepository;
    }

    public Comune findById(Long id) {
        return comuneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }


    public Comune save(String nomeComune,
                       String codiceProvincia,
                       String progressivoComune,
                       Provincia provincia) {

    }
}
