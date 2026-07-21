package noemicoppotelli.finalbuildweek.cliente;

import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Crea un nuovo cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente creaCliente(
            @RequestBody Cliente cliente
    ) {
        return clienteService.salvaCliente(cliente);
    }

    // Restituisce tutti i clienti
    @GetMapping
    public List<Cliente> trovaTutti() {
        return clienteService.trovaTutti();
    }

    // Cerca un cliente per id
    @GetMapping("/{id}")
    public Cliente trovaPerId(
            @PathVariable Long id
    ) {
        return clienteService.trovaPerId(id);
    }

    // Elimina un cliente
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminaCliente(
            @PathVariable Long id
    ) {
        clienteService.eliminaCliente(id);
    }

    // Carica il logo aziendale su Cloudinary
    @PostMapping("/{id}/logo")
    public Cliente caricaLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        return clienteService.caricaLogo(id, file);
    }
}