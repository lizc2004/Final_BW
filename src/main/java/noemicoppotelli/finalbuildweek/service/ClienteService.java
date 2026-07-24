package noemicoppotelli.finalbuildweek.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
import noemicoppotelli.finalbuildweek.specifications.ClienteSpecification;
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

    // Repository utilizzato per comunicare con il database.
    private final ClienteRepository clienteRepository;

    // Service utilizzato per caricare le immagini su Cloudinary.
    private final CloudinaryService cloudinaryService;

    /*
     * Dependency Injection tramite costruttore.
     */
    public ClienteService(
            ClienteRepository clienteRepository,
            CloudinaryService cloudinaryService
    ) {
        this.clienteRepository = clienteRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /*
     * Crea e salva un nuovo cliente.
     */
    public ClienteResponseDTO salvaCliente(
            ClientePayloadDTO payload
    ) {

        /*
         * Converte il DTO ricevuto dal controller
         * in una nuova entity Cliente.
         */
        Cliente nuovoCliente = Cliente.builder()
                .ragioneSociale(payload.ragioneSociale())
                .email(payload.email())
                .partitaIva(payload.partitaIva())
                .codiceFiscale(payload.codiceFiscale())
                .dataUltimoContatto(
                        payload.dataUltimoContatto()
                )
                .fatturatoAnnuale(
                        payload.fatturatoAnnuale()
                )
                .pec(payload.pec())
                .telefono(payload.telefono())
                .emailContatto(payload.emailContatto())
                .nomeContatto(payload.nomeContatto())
                .cognomeContatto(payload.cognomeContatto())
                .telefonoContatto(
                        payload.telefonoContatto()
                )
                .tipoCliente(payload.tipoCliente())
                .build();

        // Salva il cliente nel database.
        Cliente clienteSalvato =
                clienteRepository.save(nuovoCliente);

        // Restituisce il DTO del cliente salvato.
        return convertiInResponseDTO(clienteSalvato);
    }

    /*
     * Restituisce tutti i clienti presenti nel database.
     */
    public List<ClienteResponseDTO> trovaTutti() {

        return clienteRepository.findAll()
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    /*
     * Cerca i clienti applicando dinamicamente
     * soltanto i filtri ricevuti.
     *
     * Tutte le condizioni vengono combinate con AND.
     */
    public List<ClienteResponseDTO> filtraClienti(
            String ragioneSociale,
            BigDecimal fatturatoMin,
            BigDecimal fatturatoMax,
            LocalDateTime dataInserimentoDa,
            LocalDateTime dataInserimentoA,
            LocalDateTime dataUltimoContattoDa,
            LocalDateTime dataUltimoContattoA
    ) {

        /*
         * La prima Specification rappresenta il filtro
         * sulla ragione sociale.
         *
         * Gli altri filtri vengono concatenati con and().
         * Se un parametro è null, la relativa Specification
         * restituisce una condizione sempre vera.
         */
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

        /*
         * Esegue la query dinamica e converte
         * le entity trovate in DTO.
         */
        return clienteRepository.findAll(specification)
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    /*
     * Ordina i clienti in base al campo
     * e alla direzione ricevuti.
     *
     * Campi disponibili:
     * - nome
     * - fatturato
     * - dataInserimento
     * - dataUltimoContatto
     * - provincia
     *
     * Direzioni disponibili:
     * - asc
     * - desc
     */
    public List<ClienteResponseDTO> ordinaClienti(
            String campo,
            String direzione
    ) {

        // Normalizza il nome del campo ricevuto.
        String campoNormalizzato =
                normalizzaCampo(campo);

        // Converte asc o desc nell'enum di Spring.
        Sort.Direction sortDirection =
                convertiDirezioneOrdinamento(direzione);

        /*
         * La provincia non è un attributo diretto di Cliente.
         *
         * Il percorso da seguire è:
         * Cliente → Indirizzo → Comune → Provincia.
         *
         * Per questo motivo viene utilizzata
         * una Specification con i join necessari.
         */
        if (campoNormalizzato.equals("provincia")) {

            Specification<Cliente> ordinamentoProvincia =
                    ordinaPerProvinciaSedeLegale(
                            sortDirection
                    );

            return clienteRepository
                    .findAll(ordinamentoProvincia)
                    .stream()
                    .map(this::convertiInResponseDTO)
                    .toList();
        }

        /*
         * Per gli altri campi è sufficiente
         * utilizzare Sort di Spring Data.
         */
        String campoEntity =
                convertiCampoOrdinamento(
                        campoNormalizzato
                );

        Sort.Order ordine =
                new Sort.Order(
                        sortDirection,
                        campoEntity
                );

        /*
         * Quando ordiniamo per ragione sociale,
         * ignoriamo maiuscole e minuscole.
         */
        if (campoEntity.equals("ragioneSociale")) {
            ordine = ordine.ignoreCase();
        }

        Sort sort = Sort.by(ordine);

        return clienteRepository.findAll(sort)
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    /*
     * Restituisce un cliente tramite id.
     *
     * Se il cliente non esiste viene lanciata
     * la NotFoundException personalizzata.
     */
    public Cliente trovaPerId(Long id) {

        return clienteRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(id)
                );
    }

    /*
     * Modifica i dati di un cliente esistente.
     */
    public ClienteResponseDTO modificaCliente(
            Long id,
            ClientePayloadDTO payload
    ) {

        // Recupera il cliente da modificare.
        Cliente clienteTrovato =
                trovaEntityPerId(id);

        // Aggiorna i campi dell'entity.
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

        // Salva le modifiche nel database.
        Cliente clienteAggiornato =
                clienteRepository.save(clienteTrovato);

        return convertiInResponseDTO(
                clienteAggiornato
        );
    }

    /*
     * Elimina un cliente tramite id.
     */
    public void eliminaCliente(Long id) {

        Cliente clienteTrovato =
                trovaEntityPerId(id);

        clienteRepository.delete(clienteTrovato);
    }

    /*
     * Carica il logo aziendale su Cloudinary
     * e salva nel cliente l'URL restituito.
     */
    public ClienteResponseDTO caricaLogo(
            Long id,
            MultipartFile file
    ) throws IOException {

        // Recupera il cliente.
        Cliente clienteTrovato =
                trovaEntityPerId(id);

        // Carica l'immagine su Cloudinary.
        String urlLogo =
                cloudinaryService.uploadImage(file);

        // Salva l'URL del logo nell'entity.
        clienteTrovato.setLogoAziendale(urlLogo);

        // Aggiorna il cliente nel database.
        Cliente clienteAggiornato =
                clienteRepository.save(clienteTrovato);

        return convertiInResponseDTO(
                clienteAggiornato
        );
    }

    /*
     * Crea una Specification che ordina i clienti
     * per nome della provincia della sede legale.
     */
    private Specification<Cliente>
    ordinaPerProvinciaSedeLegale(
            Sort.Direction direzione
    ) {

        return (root, query, criteriaBuilder) -> {

            /*
             * Join:
             * Cliente → Indirizzo.
             */
            Join<Cliente, Indirizzo> indirizzoJoin =
                    root.join(
                            "indirizzi",
                            JoinType.LEFT
                    );

            /*
             * Del cliente consideriamo soltanto
             * l'indirizzo di tipo SEDE_LEGALE.
             */
            indirizzoJoin.on(
                    criteriaBuilder.equal(
                            indirizzoJoin.get(
                                    "tipoIndirizzo"
                            ),
                            TipoIndirizzo.SEDE_LEGALE
                    )
            );

            /*
             * Join:
             * Indirizzo → Comune.
             */
            Join<Indirizzo, Comune> comuneJoin =
                    indirizzoJoin.join(
                            "comune",
                            JoinType.LEFT
                    );

            /*
             * Join:
             * Comune → Provincia.
             */
            Join<Comune, Provincia> provinciaJoin =
                    comuneJoin.join(
                            "provincia",
                            JoinType.LEFT
                    );

            /*
             * L'entity Provincia utilizza il campo "name"
             * per memorizzare il nome della provincia.
             *
             * lower rende l'ordinamento indipendente
             * da maiuscole e minuscole.
             */
            var nomeProvincia =
                    criteriaBuilder.lower(
                            provinciaJoin.get("name")
                    );

            // Imposta l'ordinamento crescente o decrescente.
            if (direzione == Sort.Direction.DESC) {

                query.orderBy(
                        criteriaBuilder.desc(
                                nomeProvincia
                        )
                );

            } else {

                query.orderBy(
                        criteriaBuilder.asc(
                                nomeProvincia
                        )
                );
            }

            /*
             * Non viene aggiunta una condizione WHERE.
             *
             * Questa Specification serve esclusivamente
             * per creare i join e impostare l'ordinamento.
             */
            return criteriaBuilder.conjunction();
        };
    }

    /*
     * Normalizza il nome del campo ricevuto nell'URL.
     *
     * Esempi equivalenti:
     * - dataInserimento
     * - data_inserimento
     * - data-inserimento
     * - data inserimento
     */
    private String normalizzaCampo(
            String campo
    ) {

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

    /*
     * Converte il nome utilizzato nell'endpoint
     * nel nome reale dell'attributo dell'entity Cliente.
     */
    private String convertiCampoOrdinamento(
            String campoNormalizzato
    ) {

        return switch (campoNormalizzato) {

            case "nome", "ragionesociale" -> "ragioneSociale";

            case "fatturato", "fatturatoannuale" -> "fatturatoAnnuale";

            case "datainserimento" -> "dataInserimento";

            case "dataultimocontatto" -> "dataUltimoContatto";

            default -> throw new BadRequestException(
                    "Campo di ordinamento non valido. "
                            + "Valori consentiti: nome, "
                            + "fatturato, dataInserimento, "
                            + "dataUltimoContatto, provincia."
            );
        };
    }

    /*
     * Converte la direzione ricevuta dall'endpoint
     * in un valore Sort.Direction.
     */
    private Sort.Direction convertiDirezioneOrdinamento(
            String direzione
    ) {

        /*
         * Se non viene indicata una direzione,
         * viene utilizzato l'ordinamento crescente.
         */
        if (direzione == null || direzione.isBlank()) {
            return Sort.Direction.ASC;
        }

        return switch (
                direzione.trim().toLowerCase()
                ) {

            case "asc", "crescente" -> Sort.Direction.ASC;

            case "desc", "decrescente" -> Sort.Direction.DESC;

            default -> throw new BadRequestException(
                    "Direzione di ordinamento non valida. "
                            + "Utilizzare asc oppure desc."
            );
        };
    }

    /*
     * Recupera direttamente l'entity Cliente tramite id.
     */
    private Cliente trovaEntityPerId(Long id) {

        return clienteRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(id)
                );
    }

    /*
     * Converte l'entity Cliente
     * nel DTO restituito dagli endpoint.
     */
    private ClienteResponseDTO convertiInResponseDTO(
            Cliente cliente
    ) {

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
                cliente.getTipoCliente()
        );
    }

    public Cliente trovaPerEmail(String email) {
        return clienteRepository.findByEmailOrEmailContatto(email, email)
                .orElseThrow(() -> new NotFoundException("Nessun cliente trovato con email: " + email));
    }
}