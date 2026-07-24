package noemicoppotelli.finalbuildweek.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.payloads.ClientePayloadDTO;
import noemicoppotelli.finalbuildweek.payloads.ClienteResponseDTO;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import noemicoppotelli.finalbuildweek.repositories.IndirizzoRepository;
import noemicoppotelli.finalbuildweek.specifications.ClienteSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ComuneRepository comuneRepository;
    private final IndirizzoRepository indirizzoRepository;
    private final CloudinaryService cloudinaryService;

    public ClienteService(
            ClienteRepository clienteRepository,
            ComuneRepository comuneRepository,
            IndirizzoRepository indirizzoRepository,
            CloudinaryService cloudinaryService
    ) {
        this.clienteRepository = clienteRepository;
        this.comuneRepository = comuneRepository;
        this.indirizzoRepository = indirizzoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // CREAZIONE CLIENTE

    @Transactional
    public ClienteResponseDTO salvaCliente(ClientePayloadDTO payload) {

        Comune comune = trovaComunePerId(payload.comuneId());

        Cliente nuovoCliente = Cliente.builder()
                .ragioneSociale(payload.ragioneSociale())
                .email(payload.email())
                .partitaIva(payload.partitaIva())
                .codiceFiscale(payload.codiceFiscale())
                .dataUltimoContatto(payload.dataUltimoContatto())
                .fatturatoAnnuale(payload.fatturatoAnnuale())
                .pec(payload.pec())
                .telefono(payload.telefono())
                .emailContatto(payload.emailContatto())
                .nomeContatto(payload.nomeContatto())
                .cognomeContatto(payload.cognomeContatto())
                .telefonoContatto(payload.telefonoContatto())
                .tipoCliente(payload.tipoCliente())
                .build();

        Cliente clienteSalvato =
                clienteRepository.save(nuovoCliente);

        Indirizzo sedeLegale = new Indirizzo(
                clienteSalvato,
                TipoIndirizzo.SEDE_LEGALE,
                payload.via(),
                payload.civico(),
                payload.cap(),
                comune
        );

        Indirizzo indirizzoSalvato =
                indirizzoRepository.save(sedeLegale);

        clienteSalvato.getIndirizzi().add(indirizzoSalvato);

        return convertiInResponseDTO(clienteSalvato);
    }

    // PAGINAZIONE

    public Page<ClienteResponseDTO> trovaTutti(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        if (page < 0) {
            throw new BadRequestException(
                    "Il numero della pagina non può essere negativo."
            );
        }

        if (size <= 0) {
            throw new BadRequestException(
                    "Il numero di clienti per pagina deve essere maggiore di zero."
            );
        }

        String campoNormalizzato =
                normalizzaCampo(sortBy);

        String campoEntity =
                convertiCampoOrdinamento(campoNormalizzato);

        Sort.Direction sortDirection =
                convertiDirezioneOrdinamento(direction);

        Sort.Order ordine =
                new Sort.Order(sortDirection, campoEntity);

        if (campoEntity.equals("ragioneSociale")) {
            ordine = ordine.ignoreCase();
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(ordine)
        );

        return clienteRepository
                .findAll(pageable)
                .map(this::convertiInResponseDTO);
    }

    // FILTRI

    public List<ClienteResponseDTO> filtraClienti(
            String ragioneSociale,
            BigDecimal fatturatoMin,
            BigDecimal fatturatoMax,
            LocalDateTime dataInserimentoDa,
            LocalDateTime dataInserimentoA,
            LocalDateTime dataUltimoContattoDa,
            LocalDateTime dataUltimoContattoA
    ) {

        Specification<Cliente> specification =
                ClienteSpecification
                        .ragioneSocialeContiene(ragioneSociale)
                        .and(
                                ClienteSpecification
                                        .fatturatoMaggioreUguale(
                                                fatturatoMin
                                        )
                        )
                        .and(
                                ClienteSpecification
                                        .fatturatoMinoreUguale(
                                                fatturatoMax
                                        )
                        )
                        .and(
                                ClienteSpecification
                                        .dataInserimentoDa(
                                                dataInserimentoDa
                                        )
                        )
                        .and(
                                ClienteSpecification
                                        .dataInserimentoA(
                                                dataInserimentoA
                                        )
                        )
                        .and(
                                ClienteSpecification
                                        .dataUltimoContattoDa(
                                                dataUltimoContattoDa
                                        )
                        )
                        .and(
                                ClienteSpecification
                                        .dataUltimoContattoA(
                                                dataUltimoContattoA
                                        )
                        );

        return clienteRepository
                .findAll(specification)
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    // ORDINAMENTO

    public List<ClienteResponseDTO> ordinaClienti(
            String campo,
            String direzione
    ) {

        String campoNormalizzato =
                normalizzaCampo(campo);

        Sort.Direction sortDirection =
                convertiDirezioneOrdinamento(direzione);

        if (campoNormalizzato.equals("provincia")) {

            Specification<Cliente> ordinamentoProvincia =
                    ordinaPerProvinciaSedeLegale(sortDirection);

            return clienteRepository
                    .findAll(ordinamentoProvincia)
                    .stream()
                    .map(this::convertiInResponseDTO)
                    .toList();
        }

        String campoEntity =
                convertiCampoOrdinamento(campoNormalizzato);

        Sort.Order ordine =
                new Sort.Order(sortDirection, campoEntity);

        if (campoEntity.equals("ragioneSociale")) {
            ordine = ordine.ignoreCase();
        }

        Sort sort = Sort.by(ordine);

        return clienteRepository
                .findAll(sort)
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    // RICERCA PER ID


    public Cliente trovaPerId(Long id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(id)
                );
    }

    // MODIFICA CLIENTE

    @Transactional
    public ClienteResponseDTO modificaCliente(
            Long id,
            ClientePayloadDTO payload
    ) {

        Cliente clienteTrovato =
                trovaEntityPerId(id);

        Comune comune =
                trovaComunePerId(payload.comuneId());

        clienteTrovato.setRagioneSociale(
                payload.ragioneSociale()
        );

        clienteTrovato.setEmail(
                payload.email()
        );

        clienteTrovato.setPartitaIva(
                payload.partitaIva()
        );

        clienteTrovato.setCodiceFiscale(
                payload.codiceFiscale()
        );

        clienteTrovato.setDataUltimoContatto(
                payload.dataUltimoContatto()
        );

        clienteTrovato.setFatturatoAnnuale(
                payload.fatturatoAnnuale()
        );

        clienteTrovato.setPec(
                payload.pec()
        );

        clienteTrovato.setTelefono(
                payload.telefono()
        );

        clienteTrovato.setEmailContatto(
                payload.emailContatto()
        );

        clienteTrovato.setNomeContatto(
                payload.nomeContatto()
        );

        clienteTrovato.setCognomeContatto(
                payload.cognomeContatto()
        );

        clienteTrovato.setTelefonoContatto(
                payload.telefonoContatto()
        );

        clienteTrovato.setTipoCliente(
                payload.tipoCliente()
        );

        Cliente clienteAggiornato =
                clienteRepository.save(clienteTrovato);

        /* Cerchiamo l'indirizzo di tipo SEDE_LEGALE già associato.
         * Se esiste lo aggiorniamo.
         * Se non esiste lo creiamo.
         */
        Indirizzo sedeLegale = clienteTrovato
                .getIndirizzi()
                .stream()
                .filter(indirizzo ->
                        indirizzo.getTipoIndirizzo()
                                == TipoIndirizzo.SEDE_LEGALE
                )
                .findFirst()
                .orElse(null);

        if (sedeLegale == null) {

            sedeLegale = new Indirizzo(
                    clienteAggiornato,
                    TipoIndirizzo.SEDE_LEGALE,
                    payload.via(),
                    payload.civico(),
                    payload.cap(),
                    comune
            );

            Indirizzo indirizzoSalvato =
                    indirizzoRepository.save(sedeLegale);

            clienteAggiornato
                    .getIndirizzi()
                    .add(indirizzoSalvato);

        } else {

            sedeLegale.setVia(payload.via());
            sedeLegale.setCivico(payload.civico());
            sedeLegale.setCap(payload.cap());
            sedeLegale.setComune(comune);
            sedeLegale.setLocalita(comune.getNome());

            indirizzoRepository.save(sedeLegale);
        }

        return convertiInResponseDTO(clienteAggiornato);
    }

    // ELIMINAZIONE

    public void eliminaCliente(Long id) {

        Cliente clienteTrovato =
                trovaEntityPerId(id);

        clienteRepository.delete(clienteTrovato);
    }

    // CARICAMENTO LOGO

    public ClienteResponseDTO caricaLogo(
            Long id,
            MultipartFile file
    ) throws IOException {

        Cliente clienteTrovato =
                trovaEntityPerId(id);

        String urlLogo =
                cloudinaryService.uploadImage(file);

        clienteTrovato.setLogoAziendale(urlLogo);

        Cliente clienteAggiornato =
                clienteRepository.save(clienteTrovato);

        return convertiInResponseDTO(clienteAggiornato);
    }

    // ORDINAMENTO PER PROVINCIA

    private Specification<Cliente> ordinaPerProvinciaSedeLegale(
            Sort.Direction direzione
    ) {

        return (root, query, criteriaBuilder) -> {

            Join<Cliente, Indirizzo> indirizzoJoin =
                    root.join(
                            "indirizzi",
                            JoinType.LEFT
                    );

            indirizzoJoin.on(
                    criteriaBuilder.equal(
                            indirizzoJoin.get("tipoIndirizzo"),
                            TipoIndirizzo.SEDE_LEGALE
                    )
            );

            Join<Indirizzo, Comune> comuneJoin =
                    indirizzoJoin.join(
                            "comune",
                            JoinType.LEFT
                    );

            Join<Comune, Provincia> provinciaJoin =
                    comuneJoin.join(
                            "provincia",
                            JoinType.LEFT
                    );

            var nomeProvincia =
                    criteriaBuilder.lower(
                            provinciaJoin.get("name")
                    );

            if (direzione == Sort.Direction.DESC) {

                query.orderBy(
                        criteriaBuilder.desc(nomeProvincia)
                );

            } else {

                query.orderBy(
                        criteriaBuilder.asc(nomeProvincia)
                );
            }

            return criteriaBuilder.conjunction();
        };
    }

    // NORMALIZZAZIONE CAMPO

    private String normalizzaCampo(String campo) {

        if (campo == null || campo.isBlank()) {
            throw new BadRequestException(
                    "Il campo di ordinamento è obbligatorio."
            );
        }

        return campo
                .trim()
                .replace("-", "")
                .replace("_", "")
                .replace(" ", "")
                .toLowerCase();
    }

    // CONVERSIONE CAMPO DI ORDINAMENTO

    private String convertiCampoOrdinamento(
            String campoNormalizzato
    ) {

        return switch (campoNormalizzato) {

            case "nome", "ragionesociale" ->
                    "ragioneSociale";

            case "fatturato", "fatturatoannuale" ->
                    "fatturatoAnnuale";

            case "datainserimento" ->
                    "dataInserimento";

            case "dataultimocontatto" ->
                    "dataUltimoContatto";

            default ->
                    throw new BadRequestException(
                            "Campo di ordinamento non valido. "
                                    + "Valori consentiti: nome, "
                                    + "fatturato, dataInserimento, "
                                    + "dataUltimoContatto, provincia."
                    );
        };
    }

    // CONVERSIONE DIREZIONE

    private Sort.Direction convertiDirezioneOrdinamento(
            String direzione
    ) {

        if (direzione == null || direzione.isBlank()) {
            return Sort.Direction.ASC;
        }

        return switch (
                direzione.trim().toLowerCase()
                ) {

            case "asc", "crescente" ->
                    Sort.Direction.ASC;

            case "desc", "decrescente" ->
                    Sort.Direction.DESC;

            default ->
                    throw new BadRequestException(
                            "Direzione di ordinamento non valida. "
                                    + "Utilizzare asc oppure desc."
                    );
        };
    }

    // TROVA CLIENTE ENTITY

    private Cliente trovaEntityPerId(Long id) {

        return clienteRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(id)
                );
    }

    // TROVA COMUNE

    private Comune trovaComunePerId(Long comuneId) {

        if (comuneId == null) {
            throw new BadRequestException(
                    "Il comune della sede legale è obbligatorio."
            );
        }

        return comuneRepository
                .findById(comuneId)
                .orElseThrow(
                        () -> new BadRequestException(
                                "Comune con id "
                                        + comuneId
                                        + " non trovato."
                        )
                );
    }

    // CONVERSIONE RESPONSE DTO


    private ClienteResponseDTO convertiInResponseDTO(
            Cliente cliente
    ) {

        String provincia = cliente
                .getIndirizzi()
                .stream()
                .filter(indirizzo ->
                        indirizzo.getTipoIndirizzo()
                                == TipoIndirizzo.SEDE_LEGALE
                )
                .map(Indirizzo::getComune)
                .filter(comune -> comune != null)
                .map(Comune::getProvincia)
                .filter(provinciaTrovata ->
                        provinciaTrovata != null
                )
                .map(Provincia::getName)
                .findFirst()
                .orElse(null);

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getRagioneSociale(),
                cliente.getEmail(),
                cliente.getPartitaIva(),
                cliente.getCodiceFiscale(),
                cliente.getDataInserimento(),
                cliente.getDataUltimoContatto(),
                cliente.getFatturatoAnnuale(),
                cliente.getPec(),
                cliente.getTelefono(),
                cliente.getEmailContatto(),
                cliente.getNomeContatto(),
                cliente.getCognomeContatto(),
                cliente.getTelefonoContatto(),
                cliente.getLogoAziendale(),
                cliente.getTipoCliente(),
                provincia
        );
    }
}