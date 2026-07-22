package noemicoppotelli.finalbuildweek.service;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Fattura;
import noemicoppotelli.finalbuildweek.entities.StatoFattura;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.payloads.FatturaPayloadDTO;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import noemicoppotelli.finalbuildweek.repositories.FatturaRepository;
import noemicoppotelli.finalbuildweek.specifications.FatturaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaService statoFatturaService;

    public Page<Fattura> findAll(Long clienteId, Long statoId, LocalDate data, Integer anno,
                                  BigDecimal importoMin, BigDecimal importoMax, Pageable pageable) {
        Specification<Fattura> spec = Specification
                .where(FatturaSpecification.hasCliente(clienteId))
                .and(FatturaSpecification.hasStato(statoId))
                .and(FatturaSpecification.hasData(data))
                .and(FatturaSpecification.hasAnno(anno))
                .and(FatturaSpecification.importoBetween(importoMin, importoMax));

        return fatturaRepository.findAll(spec, pageable);
    }

    public Fattura findById(Long id) {
        return fatturaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + id + " non trovata"));
    }

    public Fattura save(FatturaPayloadDTO payload) {
        Cliente cliente = trovaCliente(payload.clienteId());
        StatoFattura statoFattura = statoFatturaService.findById(payload.statoFatturaId());

        Fattura fattura = Fattura.builder()
                .data(payload.data())
                .importo(payload.importo())
                .numero(payload.numero())
                .cliente(cliente)
                .statoFattura(statoFattura)
                .build();

        return fatturaRepository.save(fattura);
    }

    public Fattura update(Long id, FatturaPayloadDTO payload) {
        Fattura fattura = findById(id);
        Cliente cliente = trovaCliente(payload.clienteId());
        StatoFattura statoFattura = statoFatturaService.findById(payload.statoFatturaId());

        fattura.setData(payload.data());
        fattura.setImporto(payload.importo());
        fattura.setNumero(payload.numero());
        fattura.setCliente(cliente);
        fattura.setStatoFattura(statoFattura);

        return fatturaRepository.save(fattura);
    }

    public void delete(Long id) {
        Fattura fattura = findById(id);
        fatturaRepository.delete(fattura);
    }

    private Cliente trovaCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + clienteId + " non trovato"));
    }
}
