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

    // Dependency Injection
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Crea un nuovo cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO salvaCliente(
            @Valid @RequestBody ClientePayloadDTO payload
    ) {

        return clienteService.salvaCliente(payload);
    }

    // Restituisce tutti i clienti
    @GetMapping
    public List<ClienteResponseDTO> trovaTutti() {

        return clienteService.trovaTutti();
    }

    // Restituisce un cliente tramite id
    @GetMapping("/{id}")
    public Cliente trovaPerId(
            @PathVariable Long id
    ) {

        return clienteService.trovaPerId(id);
    }

    // Aggiorna i dati di un cliente
    @PutMapping("/{id}")
    public ClienteResponseDTO modificaCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClientePayloadDTO payload
    ) {

        return clienteService.modificaCliente(id, payload);
    }

    // Elimina un cliente
    @DeleteMapping("/{id}")
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
}