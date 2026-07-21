package noemicoppotelli.finalbuildweek.entities;

import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import noemicoppotelli.finalbuildweek.repositories.ProvinciaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@Order(2)
public class ComuneInitializer implements CommandLineRunner {
    private final ComuneRepository comuneRepository;
    private final ProvinciaRepository provinciaRepository;

    public ComuneInitializer(ComuneRepository comuneRepository, ProvinciaRepository provinciaRepository) {
        this.comuneRepository = comuneRepository;
        this.provinciaRepository = provinciaRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (comuneRepository.count() > 0) {
            return;
        }


        Resource resource =
                new ClassPathResource("Data/comuni-italiani.csv");


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


                    String codiceProvincia = data[0].trim();
                    String progressivoComune = data[1].trim();
                    String nomeComune = data[2].trim();
                    String nomeProvincia = data[3].trim();

                    if (nomeProvincia.equals("Valle d'Aosta/Vallée d'Aoste")) {
                        nomeProvincia = "Aosta";
                    } else if (nomeProvincia.equals("Verbano-Cusio-Ossola")) {
                        nomeProvincia = "Verbania";
                    } else if (nomeProvincia.equals("Monza e della Brianza")) {
                        nomeProvincia = "Monza-Brianza";
                    } else if (nomeProvincia.equals("Bolzano/Bozen")) {
                        nomeProvincia = "Bolzano";
                    } else if (nomeProvincia.equals("La Spezia")) {
                        nomeProvincia = "La-Spezia";
                    } else if (nomeProvincia.equals("Reggio nell'Emilia")) {
                        nomeProvincia = "Reggio-Emilia";
                    } else if (nomeProvincia.equals("Forlì-Cesena")) {
                        nomeProvincia = "Forli-Cesena";
                    } else if (nomeProvincia.equals("Pesaro e Urbino")) {
                        nomeProvincia = "Pesaro-Urbino";
                    } else if (nomeProvincia.equals("Ascoli Piceno")) {
                        nomeProvincia = "Ascoli-Piceno";
                    } else if (nomeProvincia.equals("Reggio Calabria")) {
                        nomeProvincia = "Reggio-Calabria";
                    } else if (nomeProvincia.equals("Vibo Valentia")) {
                        nomeProvincia = "Vibo-Valentia";
                    } else if (provinciaRepository.findByName("Sud Sardegna").isEmpty()) {

                        Provincia provincia = new Provincia();

                        provincia.setSigla("SU");
                        provincia.setName("Sud Sardegna");
                        provincia.setRegione("Sardegna");

                        provinciaRepository.save(provincia);
                    }


                    String finalNomeProvincia = nomeProvincia;
                    Provincia provincia = provinciaRepository
                            .findByName(nomeProvincia)
                            .orElseGet(() -> {

                                throw new RuntimeException(
                                        "Provincia non trovata: "
                                                + finalNomeProvincia
                                );
                            });


                    Comune comune = new Comune();

                    comune.setCodiceProvincia(codiceProvincia);
                    comune.setProgressivoComune(progressivoComune);
                    comune.setNome(nomeComune);
                    comune.setProvincia(provincia);


                    comuneRepository.save(comune);


                });


        System.out.println("Import comuni completato!");
    }
}