package noemicoppotelli.finalbuildweek.service;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.StatoFattura;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.payloads.StatoFatturaPayloadDTO;
import noemicoppotelli.finalbuildweek.repositories.StatoFatturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatoFatturaService {

    private final StatoFatturaRepository statoFatturaRepository;

    public List<StatoFattura> findAll() {
        return statoFatturaRepository.findAll();
    }

    public StatoFattura findById(Long id) {
        return statoFatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stato fattura con id " + id + " non trovato"));
    }

    public StatoFattura save(StatoFatturaPayloadDTO payload) {
        statoFatturaRepository.findByNomeIgnoreCase(payload.nome()).ifPresent(s -> {
            throw new BadRequestException("Esiste già uno stato fattura con nome " + payload.nome());
        });

        StatoFattura statoFattura = StatoFattura.builder()
                .nome(payload.nome())
                .build();

        return statoFatturaRepository.save(statoFattura);
    }
}
