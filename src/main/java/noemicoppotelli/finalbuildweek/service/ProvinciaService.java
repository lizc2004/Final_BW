package noemicoppotelli.finalbuildweek.service;

import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.repositories.ProvinciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService {
    private final ProvinciaRepository provinciaRepository;

    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    public Provincia findById(Long id) {
        return provinciaRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(id));
    }

    public Provincia findByName(String name) {
        return provinciaRepository.findByName(name)
                .orElseThrow(() ->
                        new NotFoundException("Provincia non trovata"));
    }

    public Provincia findBySigla(String sigla) {
        return provinciaRepository.findBySigla(sigla)
                .orElseThrow(() ->
                        new NotFoundException("Provincia non trovata"));
    }


    public Page<Provincia> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.provinciaRepository.findAll(pageable);
    }

    public Provincia save(Provincia provincia) {
        if (provincia.getName() == null || provincia.getName().isBlank()) {
            throw new BadRequestException("Il nome della provincia è obbligatorio");
        }
        if (provincia.getSigla() == null || provincia.getSigla().isBlank()) {
            throw new BadRequestException("La sigla è obbligatoria");
        }
        if (provincia.getSigla().length() != 2) {
            throw new BadRequestException("La sigla deve essere composta da 2 caratteri");
        }
        if (provincia.getRegione() == null || provincia.getRegione().isBlank()) {
            throw new BadRequestException("La regione è obbligatoria");
        }
        if (provinciaRepository.existsByName(provincia.getName())) {
            throw new BadRequestException("Provincia già presente");
        }
        if (provinciaRepository.existsBySigla(provincia.getSigla())) {
            throw new BadRequestException("Sigla già presente");
        }
        return provinciaRepository.save(provincia);
    }

    public boolean existByNome(String nome) {
        return this.provinciaRepository.existsByName(nome);
    }
}
