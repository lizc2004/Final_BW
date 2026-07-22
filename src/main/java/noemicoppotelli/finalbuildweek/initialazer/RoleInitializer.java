package noemicoppotelli.finalbuildweek.initialazer;

import noemicoppotelli.finalbuildweek.entities.Ruolo;
import noemicoppotelli.finalbuildweek.repositories.RuoloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(3)
public class RoleInitializer implements CommandLineRunner {
    private final RuoloRepository ruoloRepository;

    public RoleInitializer(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ruoloRepository.count() > 0) {
            return;
        }
        this.ruoloRepository.save(new Ruolo("ROLE_USER"));
        this.ruoloRepository.save(new Ruolo("ROLE_ADMIN"));
    }
}
