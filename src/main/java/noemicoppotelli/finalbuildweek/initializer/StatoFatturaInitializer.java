package noemicoppotelli.finalbuildweek.initializer;

import noemicoppotelli.finalbuildweek.entities.StatoFattura;
import noemicoppotelli.finalbuildweek.repositories.StatoFatturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class StatoFatturaInitializer implements CommandLineRunner {
    private final StatoFatturaRepository statoFatturaRepository;

    public StatoFatturaInitializer(StatoFatturaRepository statoFatturaRepository) {
        this.statoFatturaRepository = statoFatturaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (statoFatturaRepository.count() > 0) {
            return;
        }
        this.statoFatturaRepository.save(new StatoFattura("PAGATA"));
        this.statoFatturaRepository.save(new StatoFattura("NON_PAGATA"));
    }
}
