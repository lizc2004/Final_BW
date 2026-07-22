package noemicoppotelli.finalbuildweek.service;


import lombok.extern.slf4j.Slf4j;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ComuneService {
    private final ComuneRepository comuneRepository;
    private final ProvinciaService provinciaService;

    public ComuneService(ComuneRepository comuneRepository, ProvinciaService provinciaService) {
        this.comuneRepository = comuneRepository;
        this.provinciaService = provinciaService;
    }

    public Comune findById(Long id) {
        return comuneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }


    public Page<Comune> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.comuneRepository.findAll(pageable);
    }

    public Comune findByNome(String nome) {
        return comuneRepository.findByNome(nome)
                .orElseThrow(() -> new NotFoundException("Comune non trovato"));
    }

    public Comune save(Comune comune) {
        if (comune.getNome() == null || comune.getNome().isBlank()) {
            throw new BadRequestException("Il nome del comune è obbligatorio");
        }
        if (comune.getCodiceProvincia() == null || comune.getCodiceProvincia().isBlank()) {
            throw new BadRequestException("Il codice provincia è obbligatorio");
        }
        if (comune.getProgressivoComune() == null || comune.getProgressivoComune().isBlank()) {
            throw new BadRequestException("Il progressivo del comune è obbligatorio");
        }
        if (comune.getProvincia() == null) {
            throw new BadRequestException(
                    "La provincia è obbligatoria"
            );
        }

        Provincia provincia = provinciaService
                .findByName(comune.getProvincia().getName());

        comune.setProvincia(provincia);

        if (comuneRepository.existsByCodiceProvinciaAndProgressivoComune(
                comune.getCodiceProvincia(),
                comune.getProgressivoComune())) {

            throw new BadRequestException("Comune già presente" + comune);
        }

        return comuneRepository.save(comune);
    }

    public boolean existBy() {
        return this.comuneRepository.existsBy();
    }

    public List<Comune> findByProvinciaNome(String nome) {
        return comuneRepository.findByProvinciaNameIgnoreCase(nome);
    }

}
