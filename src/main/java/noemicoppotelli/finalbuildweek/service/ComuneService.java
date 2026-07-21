package noemicoppotelli.finalbuildweek.service;


import lombok.extern.slf4j.Slf4j;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

}
