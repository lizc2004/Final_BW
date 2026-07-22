package noemicoppotelli.finalbuildweek.service;


import lombok.extern.slf4j.Slf4j;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.payloads.IndirizzoDTO;
import noemicoppotelli.finalbuildweek.repositories.IndirizzoRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IndirizzoService {
    private final IndirizzoRepository indirizzoRepository;
    private final ClienteService clienteService;
    private final ComuneService comuneService;

    public IndirizzoService(IndirizzoRepository indirizzoRepository, ClienteService clienteService, ComuneService comuneService) {
        this.indirizzoRepository = indirizzoRepository;
        this.clienteService = clienteService;
        this.comuneService = comuneService;
    }


    public Page<Indirizzo> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.indirizzoRepository.findAll(pageable);
    }


    public Indirizzo findById(Long Id) {
        return this.indirizzoRepository.findById(Id).orElseThrow(() -> new NotFoundException(Id));
    }


    public Indirizzo save(Long clienteId, IndirizzoDTO body) throws BadRequestException {
        Cliente clienteFromDB = this.clienteService.trovaPerId(clienteId);
        Comune comuneFromDB = this.comuneService.findByNome(body.nomeComune());
        if (this.indirizzoRepository.existsByClienteIdAndTipoIndirizzo(clienteId, body.tipoIndirizzo())) {
            throw new BadRequestException(
                    "Il cliente " + clienteFromDB.getNomeContatto() + " "
                            + clienteFromDB.getCognomeContatto()
                            + " possiede già un indirizzo di tipo "
                            + body.tipoIndirizzo()
            );
        }
        if (body.tipoIndirizzo() == null) {
            throw new BadRequestException("Il tipo di indirizzo è obbligatorio");
        }
        Indirizzo newIndirizzo = new Indirizzo(clienteFromDB, body.tipoIndirizzo(), body.via(),
                body.civico(), body.cap(), comuneFromDB);

        Indirizzo saved = this.indirizzoRepository.save(newIndirizzo);
        log.info("Indirizzo {} creato per il cliente {}",
                saved.getId(),
                clienteFromDB.getRagioneSociale());
        return saved;
    }

    public Indirizzo update(Long id, IndirizzoDTO body) {
        Indirizzo indFromDB = this.findById(id);
        Comune comuneFromDB = this.comuneService.findByNome(body.nomeComune());
        indFromDB.setVia(body.via());
        indFromDB.setCivico(body.civico());
        indFromDB.setCap(body.cap());
        indFromDB.setComune(comuneFromDB);

        Indirizzo update = this.indirizzoRepository.save(indFromDB);
        return update;
    }

    public void delete(Long id) {
        Indirizzo indFromDB = this.findById(id);
        this.indirizzoRepository.delete(indFromDB);

    }

    public List<Indirizzo> findByCliente(Long clienteId) {
        return this.indirizzoRepository.findByClienteId(clienteId);
    }
}
