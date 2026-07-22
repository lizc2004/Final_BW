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

    public IndirizzoService(
            IndirizzoRepository indirizzoRepository,
            ClienteService clienteService,
            ComuneService comuneService
    ) {
        this.indirizzoRepository = indirizzoRepository;
        this.clienteService = clienteService;
        this.comuneService = comuneService;
    }

    public Page<Indirizzo> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size <= 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(orderBy)
        );

        return this.indirizzoRepository.findAll(pageable);
    }

    public Indirizzo findById(Long id) {
        return this.indirizzoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Indirizzo save(
            Long clienteId,
            IndirizzoDTO body
    ) throws BadRequestException {

        /*
         * Controlliamo prima che il tipo di indirizzo sia presente,
         * perché viene utilizzato successivamente nelle query al repository.
         */
        if (body.tipoIndirizzo() == null) {
            throw new BadRequestException(
                    "Il tipo di indirizzo è obbligatorio"
            );
        }

        Cliente clienteFromDB =
                this.clienteService.trovaPerId(clienteId);

        Comune comuneFromDB =
                this.comuneService.findByNome(body.nomeComune());

        /*
         * Contiamo quanti indirizzi possiede già il cliente.
         * Se ne ha due, blocchiamo il salvataggio.
         */
        long numeroIndirizzi =
                this.indirizzoRepository.countByClienteId(clienteId);

        if (numeroIndirizzi >= 2) {
            throw new BadRequestException(
                    "Il cliente "
                            + clienteFromDB.getRagioneSociale()
                            + " possiede già il numero massimo di indirizzi"
            );
        }

        /*
         * Controlliamo che il cliente non possieda già
         * un indirizzo dello stesso tipo.
         */
        boolean tipoGiaPresente =
                this.indirizzoRepository
                        .existsByClienteIdAndTipoIndirizzo(
                                clienteId,
                                body.tipoIndirizzo()
                        );

        if (tipoGiaPresente) {
            throw new BadRequestException(
                    "Il cliente "
                            + clienteFromDB.getRagioneSociale()
                            + " possiede già un indirizzo di tipo "
                            + body.tipoIndirizzo()
            );
        }

        Indirizzo newIndirizzo = new Indirizzo(
                clienteFromDB,
                body.tipoIndirizzo(),
                body.via(),
                body.civico(),
                body.cap(),
                comuneFromDB
        );

        Indirizzo saved =
                this.indirizzoRepository.save(newIndirizzo);

        log.info(
                "Indirizzo {} creato per il cliente {}",
                saved.getId(),
                clienteFromDB.getRagioneSociale()
        );

        return saved;
    }

    public Indirizzo update(
            Long id,
            IndirizzoDTO body
    ) throws BadRequestException {

        /*
         * Anche in modifica il tipo di indirizzo è obbligatorio.
         */
        if (body.tipoIndirizzo() == null) {
            throw new BadRequestException(
                    "Il tipo di indirizzo è obbligatorio"
            );
        }

        Indirizzo indirizzoFromDB = this.findById(id);

        Comune comuneFromDB =
                this.comuneService.findByNome(body.nomeComune());

        /*
         * Verifichiamo se il tipo di indirizzo è stato modificato.
         * Se non cambia, non serve controllare il duplicato,
         * perché il repository troverebbe l'indirizzo stesso.
         */
        boolean tipoModificato =
                indirizzoFromDB.getTipoIndirizzo()
                        != body.tipoIndirizzo();

        if (tipoModificato) {

            Long clienteId =
                    indirizzoFromDB.getCliente().getId();

            /*
             * Se il cliente possiede già un indirizzo del nuovo tipo,
             * impediamo la modifica.
             */
            boolean nuovoTipoGiaPresente =
                    this.indirizzoRepository
                            .existsByClienteIdAndTipoIndirizzo(
                                    clienteId,
                                    body.tipoIndirizzo()
                            );

            if (nuovoTipoGiaPresente) {
                throw new BadRequestException(
                        "Il cliente possiede già un indirizzo di tipo "
                                + body.tipoIndirizzo()
                );
            }
        }

        indirizzoFromDB.setTipoIndirizzo(
                body.tipoIndirizzo()
        );

        indirizzoFromDB.setVia(
                body.via()
        );

        indirizzoFromDB.setCivico(
                body.civico()
        );

        indirizzoFromDB.setCap(
                body.cap()
        );

        indirizzoFromDB.setComune(
                comuneFromDB
        );

        /*
         * Aggiorniamo anche la località, perché dipende
         * dal comune selezionato.
         */
        indirizzoFromDB.setLocalita(
                comuneFromDB.getNome()
        );

        return this.indirizzoRepository.save(
                indirizzoFromDB
        );
    }

    public void delete(Long id) {
        Indirizzo indFromDB = this.findById(id);
        this.indirizzoRepository.delete(indFromDB);
    }

    public List<Indirizzo> findByCliente(Long clienteId) {
        return this.indirizzoRepository.findByClienteId(clienteId);
    }
}