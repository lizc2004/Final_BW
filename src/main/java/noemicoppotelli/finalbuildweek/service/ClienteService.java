package noemicoppotelli.finalbuildweek.service;

import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.payloads.ClientePayloadDTO;
import noemicoppotelli.finalbuildweek.payloads.ClienteResponseDTO;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@Service
public class ClienteService {

    // Repository per comunicare con il database
    private final ClienteRepository clienteRepository;

    // Service che si occupa del caricamento delle immagini su Cloudinary
    private final CloudinaryService cloudinaryService;

    // Costruttore con Dependency Injection
    public ClienteService(
            ClienteRepository clienteRepository,
            CloudinaryService cloudinaryService
    ) {
        this.clienteRepository = clienteRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Crea e salva un nuovo cliente
    public ClienteResponseDTO salvaCliente(ClientePayloadDTO payload) {

        // Converte il Payload ricevuto in una Entity Cliente
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

        // Salva il cliente nel database
        Cliente clienteSalvato = clienteRepository.save(nuovoCliente);

        // Restituisce il ResponseDTO
        return convertiInResponseDTO(clienteSalvato);
    }

    // Restituisce la lista di tutti i clienti
    public List<ClienteResponseDTO> trovaTutti() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertiInResponseDTO)
                .toList();
    }

    // Cerca un cliente tramite id
    public ClienteResponseDTO trovaPerId(Long id) {

        Cliente cliente = trovaEntityPerId(id);

        return convertiInResponseDTO(cliente);
    }

    // Modifica un cliente esistente
    public ClienteResponseDTO modificaCliente(Long id, ClientePayloadDTO payload) {

        // Recupera il cliente dal database
        Cliente clienteTrovato = trovaEntityPerId(id);

        // Aggiorna tutti i campi con i nuovi valori
        clienteTrovato.setRagioneSociale(payload.ragioneSociale());
        clienteTrovato.setEmail(payload.email());
        clienteTrovato.setPartitaIva(payload.partitaIva());
        clienteTrovato.setCodiceFiscale(payload.codiceFiscale());
        clienteTrovato.setDataUltimoContatto(payload.dataUltimoContatto());
        clienteTrovato.setFatturatoAnnuale(payload.fatturatoAnnuale());
        clienteTrovato.setPec(payload.pec());
        clienteTrovato.setTelefono(payload.telefono());
        clienteTrovato.setEmailContatto(payload.emailContatto());
        clienteTrovato.setNomeContatto(payload.nomeContatto());
        clienteTrovato.setCognomeContatto(payload.cognomeContatto());
        clienteTrovato.setTelefonoContatto(payload.telefonoContatto());
        clienteTrovato.setTipoCliente(payload.tipoCliente());

        // Salva le modifiche
        Cliente clienteAggiornato = clienteRepository.save(clienteTrovato);

        return convertiInResponseDTO(clienteAggiornato);
    }

    // Elimina un cliente dal database
    public void eliminaCliente(Long id) {

        Cliente clienteTrovato = trovaEntityPerId(id);

        clienteRepository.delete(clienteTrovato);
    }

    // Carica il logo aziendale su Cloudinary e salva il relativo URL nel database
    public ClienteResponseDTO caricaLogo(Long id, MultipartFile file) throws IOException {

        // Recupera il cliente
        Cliente clienteTrovato = trovaEntityPerId(id);

        // Carica l'immagine su Cloudinary
        String urlLogo = cloudinaryService.uploadImage(file);

        // Salva l'URL restituito da Cloudinary
        clienteTrovato.setLogoAziendale(urlLogo);

        // Aggiorna il cliente nel database
        Cliente clienteAggiornato = clienteRepository.save(clienteTrovato);

        return convertiInResponseDTO(clienteAggiornato);
    }

    // Metodo privato che recupera direttamente l'Entity Cliente
    private Cliente trovaEntityPerId(Long id) {

        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente con id " + id + " non trovato"
                        )
                );
    }

    // Converte una Entity Cliente in ClienteResponseDTO
    private ClienteResponseDTO convertiInResponseDTO(Cliente cliente) {

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
}