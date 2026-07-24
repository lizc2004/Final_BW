package noemicoppotelli.finalbuildweek.service;

import lombok.extern.slf4j.Slf4j;
import noemicoppotelli.finalbuildweek.entities.Ruolo;
import noemicoppotelli.finalbuildweek.entities.Utente;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.payloads.PasswordChangeDTO;
import noemicoppotelli.finalbuildweek.payloads.UtenteUpdateDTO;
import noemicoppotelli.finalbuildweek.repositories.RuoloRepository;
import noemicoppotelli.finalbuildweek.repositories.UtenteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, RuoloRepository ruoloRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.ruoloRepository = ruoloRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Utente getById(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

    }


    public Page<Utente> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.utenteRepository.findAll(pageable);
    }

    public void delete(Long id) {
        Utente indFromDB = this.getById(id);
        this.utenteRepository.delete(indFromDB);
        log.info(
                "Utente {} eliminato",
                id
        );
    }


    public Utente addRole(Long userId, Long roleId) {
        Utente utente = getById(userId);

        Ruolo ruolo = ruoloRepository.findById(roleId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Ruolo non trovato"
                        )
                );
        if (utente.getRuoli().contains(ruolo)) {
            throw new BadRequestException("Ruolo già assegnato");
        }
        if (utente.getRuoli().size() >= 2) {
            throw new BadRequestException("Numero massimo di ruoli raggiunto");
        }
        utente.getRuoli().add(ruolo);
        return utenteRepository.save(utente);
    }

    public Utente removeRole(Long userId, Long roleId) throws BadRequestException {
        Utente utente = getById(userId);
        Ruolo ruolo = ruoloRepository.findById(roleId)
                .orElseThrow(() ->
                        new NotFoundException("Ruolo non trovato"));
        if (!utente.getRuoli().contains(ruolo)) {
            throw new BadRequestException(
                    "L'utente non possiede questo ruolo"
            );
        }
        if (utente.getRuoli().size() == 1) {
            throw new BadRequestException(
                    "Un utente deve avere almeno un ruolo"
            );
        }
        utente.getRuoli().remove(ruolo);
        return utenteRepository.save(utente);
    }


    public Utente update(Long id, UtenteUpdateDTO body) {
        Utente utenteFromDB = this.getById(id);
        // controllo email cambiata
        if (body.email() != null &&
                !body.email().equals(utenteFromDB.getEmail())) {
            if (utenteRepository.existsByEmail(body.email())) {
                throw new BadRequestException(
                        "Email già utilizzata"
                );
            }
            utenteFromDB.setEmail(body.email());
        }
        if (body.nome() != null) {
            utenteFromDB.setNome(body.nome());
        }
        if (body.cognome() != null) {
            utenteFromDB.setCognome(body.cognome());
        }
        if (body.avatar() != null) {
            utenteFromDB.setAvatar(body.avatar());
        }
        Utente updated = utenteRepository.save(utenteFromDB);
        log.info(
                "Utente {} aggiornato",
                updated.getUsername()
        );
        return updated;
    }

    public void changePassword(Long userId, PasswordChangeDTO body) {
        Utente utente = this.getById(userId);
        // controllo password vecchia
        if (!passwordEncoder.matches(
                body.oldPassword(),
                utente.getPassword()
        )) {
            throw new BadRequestException(
                    "La password attuale non è corretta"
            );
        }
        // controllo che la nuova sia diversa
        if (passwordEncoder.matches(
                body.newPassword(),
                utente.getPassword()
        )) {
            throw new BadRequestException(
                    "La nuova password deve essere diversa dalla precedente"
            );
        }
        // salvo nuova password criptata
        utente.setPassword(
                passwordEncoder.encode(body.newPassword())
        );
        utenteRepository.save(utente);
    }


    public Page<Utente> getAllPerNome(
            int page,
            int size,
            String nome
    ) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("id").ascending()
        );
        if (nome != null && !nome.isBlank()) {
            return utenteRepository
                    .findByNomeContainingIgnoreCase(nome, pageable);
        }


        return utenteRepository.findAll(pageable);
    }


    public boolean existBy() {
        return this.utenteRepository.existsBy();
    }
}
