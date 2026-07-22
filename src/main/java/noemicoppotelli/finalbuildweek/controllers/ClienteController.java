package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.ValidationException;
import noemicoppotelli.finalbuildweek.payloads.ClientePayloadDTO;
import noemicoppotelli.finalbuildweek.payloads.ClienteResponseDTO;
import noemicoppotelli.finalbuildweek.payloads.IndirizzoDTO;
import noemicoppotelli.finalbuildweek.service.ClienteService;
import noemicoppotelli.finalbuildweek.service.IndirizzoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    // Service che gestisce la logica relativa ai clienti.
    private final ClienteService clienteService;

    // Service che gestisce la logica relativa agli indirizzi.
    private final IndirizzoService indirizzoService;

    /*
     * Dependency Injection tramite costruttore.
     */
    public ClienteController(
            ClienteService clienteService,
            IndirizzoService indirizzoService
    ) {
        this.clienteService = clienteService;
        this.indirizzoService = indirizzoService;
    }

    /*
     * Crea un nuovo cliente.
     *
     * Endpoint:
     * POST /clienti
     *
     * Possono accedere USER e ADMIN.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ClienteResponseDTO salvaCliente(
            @Valid @RequestBody ClientePayloadDTO payload,
            BindingResult validationResult
    ) {

        // Verifica gli eventuali errori del DTO.
        controllaValidazione(validationResult);

        return clienteService.salvaCliente(payload);
    }

    /*
     * Restituisce tutti i clienti.
     *
     * Endpoint:
     * GET /clienti
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ClienteResponseDTO> trovaTutti() {

        return clienteService.trovaTutti();
    }

    /*
     * Filtra la lista dei clienti.
     *
     * Endpoint:
     * GET /clienti/filtro
     *
     * Tutti i parametri sono facoltativi.
     *
     * Esempio:
     * GET /clienti/filtro
     * ?ragioneSociale=energia
     * &fatturatoMin=10000
     */
    @GetMapping("/filtro")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ClienteResponseDTO> filtraClienti(

            // Parte della ragione sociale.
            @RequestParam(required = false)
            String ragioneSociale,

            // Fatturato annuale minimo.
            @RequestParam(required = false)
            BigDecimal fatturatoMin,

            // Fatturato annuale massimo.
            @RequestParam(required = false)
            BigDecimal fatturatoMax,

            /*
             * Data minima di inserimento.
             *
             * Formato:
             * 2026-07-22T10:30:00
             */
            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataInserimentoDa,

            // Data massima di inserimento.
            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataInserimentoA,

            // Data minima dell'ultimo contatto.
            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataUltimoContattoDa,

            // Data massima dell'ultimo contatto.
            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataUltimoContattoA
    ) {

        return clienteService.filtraClienti(
                ragioneSociale,
                fatturatoMin,
                fatturatoMax,
                dataInserimentoDa,
                dataInserimentoA,
                dataUltimoContattoDa,
                dataUltimoContattoA
        );
    }

    /*
     * Ordina la lista dei clienti.
     *
     * Endpoint:
     * GET /clienti/ordinamento
     *
     * Campi disponibili:
     * - nome
     * - fatturato
     * - dataInserimento
     * - dataUltimoContatto
     * - provincia
     *
     * Direzioni:
     * - asc
     * - desc
     *
     * Esempio:
     * GET /clienti/ordinamento
     * ?campo=provincia
     * &direzione=asc
     */
    @GetMapping("/ordinamento")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ClienteResponseDTO> ordinaClienti(

            @RequestParam
            String campo,

            @RequestParam(defaultValue = "asc")
            String direzione
    ) {

        return clienteService.ordinaClienti(
                campo,
                direzione
        );
    }

    /*
     * Restituisce un singolo cliente tramite id.
     *
     * Endpoint:
     * GET /clienti/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Cliente trovaPerId(
            @PathVariable Long id
    ) {

        return clienteService.trovaPerId(id);
    }

    /*
     * Modifica un cliente esistente.
     *
     * Endpoint:
     * PUT /clienti/{id}
     *
     * Può accedere soltanto ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteResponseDTO modificaCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClientePayloadDTO payload,
            BindingResult validationResult
    ) {

        controllaValidazione(validationResult);

        return clienteService.modificaCliente(
                id,
                payload
        );
    }

    /*
     * Elimina un cliente.
     *
     * Endpoint:
     * DELETE /clienti/{id}
     *
     * Restituisce HTTP 204 No Content.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminaCliente(
            @PathVariable Long id
    ) {

        clienteService.eliminaCliente(id);
    }

    /*
     * Carica il logo aziendale del cliente su Cloudinary.
     *
     * Endpoint:
     * POST /clienti/{id}/logo
     *
     * Il file deve essere inviato come multipart/form-data
     * utilizzando la chiave "file".
     */
    @PostMapping("/{id}/logo")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteResponseDTO caricaLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return clienteService.caricaLogo(
                id,
                file
        );
    }

    /*
     * Crea un indirizzo associato a un cliente.
     *
     * Endpoint:
     * POST /clienti/{clienteId}/indirizzi
     */
    @PostMapping("/{clienteId}/indirizzi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Indirizzo salvaIndirizzo(
            @PathVariable Long clienteId,
            @Valid @RequestBody IndirizzoDTO body,
            BindingResult validationResult
    ) throws BadRequestException, org.apache.coyote.BadRequestException {

        controllaValidazione(validationResult);

        return indirizzoService.save(
                clienteId,
                body
        );
    }

    /*
     * Recupera gli indirizzi associati a un cliente.
     *
     * Endpoint:
     * GET /clienti/{clienteId}/indirizzi
     */
    @GetMapping("/{clienteId}/indirizzi")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Indirizzo> trovaIndirizzi(
            @PathVariable Long clienteId
    ) {

        return indirizzoService.findByCliente(
                clienteId
        );
    }

    /*
     * Controlla gli errori prodotti dalla validazione.
     *
     * Viene riutilizzato negli endpoint che ricevono
     * un body annotato con @Valid.
     */
    private void controllaValidazione(
            BindingResult validationResult
    ) {

        // Se non ci sono errori, termina il metodo.
        if (!validationResult.hasErrors()) {
            return;
        }

        /*
         * Estrae tutti i messaggi di errore
         * dai campi che non hanno superato la validazione.
         */
        List<String> errorsList = validationResult
                .getFieldErrors()
                .stream()
                .map(
                        fieldError ->
                                fieldError.getDefaultMessage()
                )
                .toList();

        // Lancia l'eccezione personalizzata.
        throw new ValidationException(errorsList);
    }
}