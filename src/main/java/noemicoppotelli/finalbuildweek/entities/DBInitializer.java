package noemicoppotelli.finalbuildweek.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DBInitializer implements CommandLineRunner {

    @Autowired
    ComuneRepository comuneRepository;

    @Override
    public void run(String... args) throws Exception {

        Resource resource = new ClassPathResource(
                new InputStreamReader(resource.getInputStream())
        );

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(resource.getInputStream())
                );


        reader.lines()
                .skip(1)
                .forEach(line -> {
                    String[] data = line.split(";");

                    Comune comune = new Comune();

                    comune.setCodiceProvincia(data[0]);
                    comune.setProgressivo(data[1]);
                    comune.setDenominazione(data[2]);
                    comune.setProvincia(data[3]);

                    comuneRepository.save(comune);
                });
    }
}
