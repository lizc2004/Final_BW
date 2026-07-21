package noemicoppotelli.finalbuildweek.entities;


import noemicoppotelli.finalbuildweek.repositories.ProvinciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@Order(1)
public class ProvinciaInitializer implements CommandLineRunner {
    private final ProvinciaRepository provinciaRepository;

    public ProvinciaInitializer(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (provinciaRepository.count() == 0) {
            Resource resource =
                    new ClassPathResource("Data/province-italiane.csv");


            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    resource.getInputStream()
                            )
                    );


            reader.lines()
                    .skip(1)
                    .forEach(line -> {

                        String[] data = line.split(";");


                        Provincia provincia = new Provincia();

                        provincia.setSigla(data[0].trim());
                        provincia.setName(data[1].trim());
                        provincia.setRegione(data[2].trim());


                        provinciaRepository.save(provincia);
                    });
        }
    }
}
