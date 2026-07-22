package noemicoppotelli.finalbuildweek.initializer;

import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.service.ComuneService;
import noemicoppotelli.finalbuildweek.service.ProvinciaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(2)
public class ComuneInitializer implements CommandLineRunner {
    private final ComuneService comuneService;
    private final ProvinciaService provinciaService;

    public ComuneInitializer(ComuneService comuneService, ProvinciaService provinciaService) {
        this.comuneService = comuneService;
        this.provinciaService = provinciaService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (comuneService.existBy()) {
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
        Map<String, Integer> progressivi = new HashMap<>();
        reader.lines()
                .skip(1)
                .forEach(line -> {
                    String[] data = line.split(";");
                    String codiceProvincia = data[0].trim();
                    String progressivoComune = data[1].trim();
                    String nomeComune = data[2].trim();
                    String nomeProvincia = data[3].trim();
                    if (progressivoComune.equals("#RIF!")) {
                        int nuovoProgressivo = progressivi.getOrDefault(
                                codiceProvincia,
                                0
                        ) + 1;
                        progressivi.put(
                                codiceProvincia,
                                nuovoProgressivo
                        );
                        progressivoComune = String.format(
                                "%03d",
                                nuovoProgressivo
                        );
                    }
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
                    } else if (!provinciaService.existByNome("Sud Sardegna")) {
                        Provincia provincia = new Provincia();
                        provincia.setSigla("SU");
                        provincia.setName("Sud Sardegna");
                        provincia.setRegione("Sardegna");
                        provinciaService.save(provincia);
                    }
                    String finalNomeProvincia = nomeProvincia;
                    Provincia provincia = provinciaService
                            .findByName(nomeProvincia);
                    Comune comune = new Comune(nomeComune,
                            codiceProvincia,
                            progressivoComune,
                            provincia);
                    comuneService.save(comune);
                });
        System.out.println("Import comuni completato!");
    }
}