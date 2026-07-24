package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.ValidationException;
import noemicoppotelli.finalbuildweek.payloads.ClientePayloadDTO;
import noemicoppotelli.finalbuildweek.payloads.ClienteResponseDTO;
import noemicoppotelli.finalbuildweek.payloads.EmailRequestDTO;
import noemicoppotelli.finalbuildweek.payloads.IndirizzoDTO;
import noemicoppotelli.finalbuildweek.service.ClienteService;
import noemicoppotelli.finalbuildweek.service.EmailService;
import noemicoppotelli.finalbuildweek.service.IndirizzoService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ClienteService clienteService;

    private final IndirizzoService indirizzoService;

    private final EmailService emailService;

    public ClienteController(
            ClienteService clienteService,
            IndirizzoService indirizzoService,
            EmailService emailService
    ) {
        this.clienteService = clienteService;
        this.indirizzoService = indirizzoService;
        this.emailService = emailService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ClienteResponseDTO salvaCliente(
            @Valid @RequestBody ClientePayloadDTO payload,
            BindingResult validationResult
    ) {

        controllaValidazione(validationResult);

        return clienteService.salvaCliente(payload);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ClienteResponseDTO> trovaTutti(

            @RequestParam(defaultValue = "0")
            int page,

            // Numero di clienti presenti in ogni pagina.
            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "ragioneSociale")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {

        return clienteService.trovaTutti(
                page,
                size,
                sortBy,
                direction
        );
    }


    @GetMapping("/filtro")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ClienteResponseDTO> filtraClienti(

            @RequestParam(required = false)
            String ragioneSociale,

            @RequestParam(required = false)
            BigDecimal fatturatoMin,

            @RequestParam(required = false)
            BigDecimal fatturatoMax,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataInserimentoDa,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataInserimentoA,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime dataUltimoContattoDa,

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


    @GetMapping("/ordinamento")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
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


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
    public Cliente trovaPerId(
            @PathVariable Long id
    ) {

        return clienteService.trovaPerId(id);
    }

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


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminaCliente(
            @PathVariable Long id
    ) {

        clienteService.eliminaCliente(id);
    }


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


    @GetMapping("/{clienteId}/indirizzi")
    @PreAuthorize("hasAnyRole( 'ADMIN')")
    public List<Indirizzo> trovaIndirizzi(
            @PathVariable Long clienteId
    ) {

        return indirizzoService.findByCliente(
                clienteId
        );
    }


    private void controllaValidazione(
            BindingResult validationResult
    ) {

        if (!validationResult.hasErrors()) {
            return;
        }


        List<String> errorsList = validationResult
                .getFieldErrors()
                .stream()
                .map(
                        fieldError ->
                                fieldError.getDefaultMessage()
                )
                .toList();

        throw new ValidationException(errorsList);
    }

    @PostMapping("/{id}/email")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailToCliente(
            @PathVariable Long id,
            @Valid @RequestBody EmailRequestDTO body
    ) {
        Cliente cliente = clienteService.trovaPerId(id);

        String destinatario = cliente.getEmailContatto() != null
                ? cliente.getEmailContatto()
                : cliente.getEmail();

        emailService.inviaEmail(destinatario, body.oggetto(), body.messaggio());
    }

    @GetMapping("/cerca")
    public ResponseEntity<ClienteResponseDTO.ClienteResponseEmailDTO> cercaCliente(@RequestParam String query) {
        Cliente cliente = clienteService.trovaPerEmail(query.trim());
        return ResponseEntity.ok(new ClienteResponseDTO.ClienteResponseEmailDTO(cliente.getId(), cliente.getEmail(), cliente.getRagioneSociale()));
    }
}


