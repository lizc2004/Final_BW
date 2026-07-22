package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.payloads.ClientePayloadDTO;
import noemicoppotelli.finalbuildweek.payloads.ClienteResponseDTO;
import noemicoppotelli.finalbuildweek.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    // Service che contiene la logica di business
    private final ClienteService clienteService;
    private final IndirizzoService indirizzoService;

    // Dependency Injection
    public ClienteController(ClienteService clienteService, IndirizzoService indirizzoService) {
        this.clienteService = clienteService;
        this.indirizzoService = indirizzoService;
    }

    // Crea un nuovo cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ClienteResponseDTO salvaCliente(
            @Valid @RequestBody ClientePayloadDTO payload,
            BindingResult validationResult
    ) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        }
        return clienteService.salvaCliente(payload);
    }

    // Restituisce tutti i clienti
    @GetMapping
    public List<ClienteResponseDTO> trovaTutti() {

        return clienteService.trovaTutti();
    }

    // Restituisce un cliente tramite id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Cliente trovaPerId(
            @PathVariable Long id
    ) {
        return clienteService.trovaPerId(id);
    }

    // Aggiorna i dati di un cliente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteResponseDTO modificaCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClientePayloadDTO payload
    ) {

        return clienteService.modificaCliente(id, payload);
    }

    // Elimina un cliente
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminaCliente(
            @PathVariable Long id
    ) {

        clienteService.eliminaCliente(id);
    }

    // Carica il logo aziendale del cliente su Cloudinary
    @PostMapping("/{id}/logo")
    public ClienteResponseDTO caricaLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return clienteService.caricaLogo(id, file);
    }


    //crea indirizzo partendo da cliente
    @PostMapping("/{clienteId}/indirizzi")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")

    public Indirizzo saveIndirizzo(@PathVariable Long clienteId,
                                   @Valid @RequestBody IndirizzoDTO body,
                                   BindingResult validationResult) throws BadRequestException {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        }
        return indirizzoService.save(clienteId, body);
    }


    //recupera indirizzi del cliente
    @GetMapping("/{clienteId}/indirizzi")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Indirizzo> findIndirizzi(
            @PathVariable Long clienteId) {
        return indirizzoService.findByCliente(clienteId);
    }
}