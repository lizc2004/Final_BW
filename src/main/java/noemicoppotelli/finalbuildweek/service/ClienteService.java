package noemicoppotelli.finalbuildweek.service;

import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CloudinaryService cloudinaryService;

    public ClienteService(
            ClienteRepository clienteRepository,
            CloudinaryService cloudinaryService
    ) {
        this.clienteRepository = clienteRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Crea e salva un nuovo cliente
    public Cliente salvaCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Restituisce tutti i clienti
    public List<Cliente> trovaTutti() {
        return clienteRepository.findAll();
    }

    // Cerca un cliente tramite id
    public Cliente trovaPerId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente con id " + id + " non trovato"
                        )
                );
    }

    // Elimina un cliente
    public void eliminaCliente(Long id) {

        Cliente cliente = trovaPerId(id);

        clienteRepository.delete(cliente);
    }

    // Carica il logo su Cloudinary e salva l'URL nel cliente
    public Cliente caricaLogo(
            Long id,
            MultipartFile file
    ) throws IOException {

        Cliente cliente = trovaPerId(id);

        String urlLogo = cloudinaryService.uploadImage(file);

        cliente.setLogoAziendale(urlLogo);

        return clienteRepository.save(cliente);
    }
}