package noemicoppotelli.finalbuildweek.initializer;

import noemicoppotelli.finalbuildweek.entities.Ruolo;
import noemicoppotelli.finalbuildweek.entities.Utente;
import noemicoppotelli.finalbuildweek.exceptions.NotFoundException;
import noemicoppotelli.finalbuildweek.repositories.RuoloRepository;
import noemicoppotelli.finalbuildweek.repositories.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UtenteInitializer implements CommandLineRunner {
    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteInitializer(UtenteRepository utenteRepository, RuoloRepository ruoloRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.ruoloRepository = ruoloRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Order(4)
    public void run(String... args) throws Exception {
        if (utenteRepository.existsBy()) {
            return;
        }
        Ruolo ruoloAdmin = ruoloRepository.findByNome("ROLE_ADMIN").orElseThrow(() ->
                new NotFoundException("ROLE_ADMIN non trovato"));
        Utente Admin = new Utente("admin01", "admin@mail.com",
                passwordEncoder.encode("admin01"), "admin", "admin", Set.of(ruoloAdmin));
        utenteRepository.save(Admin);
        System.out.println("Utente admin creato con successo.");
    }
}
