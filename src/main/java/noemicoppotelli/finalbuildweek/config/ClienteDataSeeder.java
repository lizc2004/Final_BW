package noemicoppotelli.finalbuildweek.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.enums.TipoCliente;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import noemicoppotelli.finalbuildweek.repositories.ComuneRepository;
import noemicoppotelli.finalbuildweek.repositories.IndirizzoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Seeder utilizzato per inserire automaticamente:
 * - 10 clienti di prova;
 * - una sede legale per ogni cliente;
 * - una sede operativa per ogni cliente.
 *
 * Il seeder viene eseguito solamente quando
 * la tabella dei clienti è vuota.
 */
@Component
@RequiredArgsConstructor
@Order(6)
public class ClienteDataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ComuneRepository comuneRepository;
    private final IndirizzoRepository indirizzoRepository;

    @Override
    @Transactional
    public void run(String... args) {

        /*
         * Se esiste già almeno un cliente,
         * interrompiamo il seeder per evitare duplicati.
         */
        if (clienteRepository.count() > 0) {

            System.out.println(
                    "Seeder clienti non eseguito: "
                            + "nella tabella sono già presenti dei clienti."
            );

            return;
        }

        /*
         * Recuperiamo i comuni già presenti nel database.
         * Il seeder dei comuni deve essere eseguito
         * prima di questo seeder.
         */
        Comune lameziaTerme = trovaComune(
                "Lamezia Terme"
        );

        Comune catanzaro = trovaComune(
                "Catanzaro"
        );

        Comune cosenza = trovaComune(
                "Cosenza"
        );

        Comune crotone = trovaComune(
                "Crotone"
        );

        Comune viboValentia = trovaComune(
                "Vibo Valentia"
        );

        Comune reggioCalabria = trovaComuneAlternativo(
                "Reggio di Calabria",
                "Reggio Calabria"
        );


        // CLIENTI

        Cliente cliente1 = Cliente.builder()
                .ragioneSociale("Energia Calabria SRL")
                .email("info@energiacalabria.it")
                .partitaIva("01234567890")
                .codiceFiscale("01234567890")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                1,
                                10,
                                9,
                                0
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                15,
                                10,
                                30
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("150000.00")
                )
                .pec("pec@energiacalabria.it")
                .telefono("0968123456")
                .emailContatto(
                        "mario.rossi@energiacalabria.it"
                )
                .nomeContatto("Mario")
                .cognomeContatto("Rossi")
                .telefonoContatto("3331111111")
                .tipoCliente(TipoCliente.SRL)
                .build();


        Cliente cliente2 = Cliente.builder()
                .ragioneSociale("Edil Sud SAS")
                .email("info@edilsud.it")
                .partitaIva("01234567891")
                .codiceFiscale("01234567891")
                .dataInserimento(
                        LocalDateTime.of(
                                2025,
                                11,
                                22,
                                8,
                                30
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                6,
                                30,
                                16,
                                15
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("82000.00")
                )
                .pec("pec@edilsud.it")
                .telefono("0984123456")
                .emailContatto(
                        "luca.bianchi@edilsud.it"
                )
                .nomeContatto("Luca")
                .cognomeContatto("Bianchi")
                .telefonoContatto("3332222222")
                .tipoCliente(TipoCliente.SAS)
                .build();

        Cliente cliente3 = Cliente.builder()
                .ragioneSociale("Tech Vision SPA")
                .email("info@techvision.it")
                .partitaIva("01234567892")
                .codiceFiscale("01234567892")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                3,
                                18,
                                9,
                                45
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                20,
                                11,
                                20
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("325000.00")
                )
                .pec("pec@techvision.it")
                .telefono("0965123456")
                .emailContatto(
                        "anna.verdi@techvision.it"
                )
                .nomeContatto("Anna")
                .cognomeContatto("Verdi")
                .telefonoContatto("3333333333")
                .tipoCliente(TipoCliente.SPA)
                .build();

        Cliente cliente4 = Cliente.builder()
                .ragioneSociale("Comune di Lamezia Terme")
                .email("protocollo@comune.it")
                .partitaIva("01234567893")
                .codiceFiscale("01234567893")
                .dataInserimento(
                        LocalDateTime.of(
                                2025,
                                8,
                                15,
                                8,
                                0
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                18,
                                9,
                                30
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("500000.00")
                )
                .pec("protocollo@pec.comune.it")
                .telefono("0968200000")
                .emailContatto(
                        "segreteria@comune.it"
                )
                .nomeContatto("Giuseppe")
                .cognomeContatto("Greco")
                .telefonoContatto("3334444444")
                .tipoCliente(TipoCliente.PA)
                .build();

        Cliente cliente5 = Cliente.builder()
                .ragioneSociale("Beta Consulting SRL")
                .email("info@betaconsulting.it")
                .partitaIva("01234567894")
                .codiceFiscale("01234567894")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                2,
                                5,
                                10,
                                15
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                2,
                                14,
                                40
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("97000.00")
                )
                .pec("pec@betaconsulting.it")
                .telefono("0967123456")
                .emailContatto(
                        "sara.russo@betaconsulting.it"
                )
                .nomeContatto("Sara")
                .cognomeContatto("Russo")
                .telefonoContatto("3335555555")
                .tipoCliente(TipoCliente.SRL)
                .build();


        Cliente cliente6 = Cliente.builder()
                .ragioneSociale("Gamma Energy SRL")
                .email("info@gammaenergy.it")
                .partitaIva("01234567895")
                .codiceFiscale("01234567895")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                4,
                                11,
                                11,
                                30
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                19,
                                17,
                                10
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("210000.00")
                )
                .pec("pec@gammaenergy.it")
                .telefono("0968123999")
                .emailContatto(
                        "giulia.gallo@gammaenergy.it"
                )
                .nomeContatto("Giulia")
                .cognomeContatto("Gallo")
                .telefonoContatto("3336666666")
                .tipoCliente(TipoCliente.SRL)
                .build();


        Cliente cliente7 = Cliente.builder()
                .ragioneSociale("Delta Costruzioni SPA")
                .email("info@deltacostruzioni.it")
                .partitaIva("01234567896")
                .codiceFiscale("01234567896")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                1,
                                28,
                                8,
                                45
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                6,
                                28,
                                15,
                                0
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("180000.00")
                )
                .pec("pec@deltacostruzioni.it")
                .telefono("0985123456")
                .emailContatto(
                        "franco.esposito@deltacostruzioni.it"
                )
                .nomeContatto("Franco")
                .cognomeContatto("Esposito")
                .telefonoContatto("3337777777")
                .tipoCliente(TipoCliente.SPA)
                .build();


        Cliente cliente8 = Cliente.builder()
                .ragioneSociale("Epsilon Informatica SRL")
                .email("info@epsilon.it")
                .partitaIva("01234567897")
                .codiceFiscale("01234567897")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                5,
                                3,
                                9,
                                20
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                21,
                                12,
                                0
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("125000.00")
                )
                .pec("pec@epsilon.it")
                .telefono("0969123456")
                .emailContatto(
                        "elena.greco@epsilon.it"
                )
                .nomeContatto("Elena")
                .cognomeContatto("Greco")
                .telefonoContatto("3338888888")
                .tipoCliente(TipoCliente.SRL)
                .build();


        Cliente cliente9 = Cliente.builder()
                .ragioneSociale("Omega Service SAS")
                .email("info@omegaservice.it")
                .partitaIva("01234567898")
                .codiceFiscale("01234567898")
                .dataInserimento(
                        LocalDateTime.of(
                                2025,
                                12,
                                10,
                                13,
                                10
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                5,
                                15,
                                10,
                                45
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("67000.00")
                )
                .pec("pec@omegaservice.it")
                .telefono("0962123456")
                .emailContatto(
                        "davide.marino@omegaservice.it"
                )
                .nomeContatto("Davide")
                .cognomeContatto("Marino")
                .telefonoContatto("3339999999")
                .tipoCliente(TipoCliente.SAS)
                .build();


        Cliente cliente10 = Cliente.builder()
                .ragioneSociale("Studio Romano SRL")
                .email("info@studioromano.it")
                .partitaIva("01234567899")
                .codiceFiscale("01234567899")
                .dataInserimento(
                        LocalDateTime.of(
                                2026,
                                6,
                                1,
                                10,
                                0
                        )
                )
                .dataUltimoContatto(
                        LocalDateTime.of(
                                2026,
                                7,
                                22,
                                9,
                                15
                        )
                )
                .fatturatoAnnuale(
                        new BigDecimal("56000.00")
                )
                .pec("pec@studioromano.it")
                .telefono("0968123888")
                .emailContatto(
                        "laura.romano@studioromano.it"
                )
                .nomeContatto("Laura")
                .cognomeContatto("Romano")
                .telefonoContatto("3331234567")
                .tipoCliente(TipoCliente.SRL)
                .build();

        /* Salviamo tutti i clienti. */
        List<Cliente> clientiSalvati =
                clienteRepository.saveAll(
                        List.of(
                                cliente1,
                                cliente2,
                                cliente3,
                                cliente4,
                                cliente5,
                                cliente6,
                                cliente7,
                                cliente8,
                                cliente9,
                                cliente10
                        )
                );

        /* Recuperiamo i clienti salvati rispettando lo stesso ordine della lista. */
        Cliente clienteSalvato1 = clientiSalvati.get(0);
        Cliente clienteSalvato2 = clientiSalvati.get(1);
        Cliente clienteSalvato3 = clientiSalvati.get(2);
        Cliente clienteSalvato4 = clientiSalvati.get(3);
        Cliente clienteSalvato5 = clientiSalvati.get(4);
        Cliente clienteSalvato6 = clientiSalvati.get(5);
        Cliente clienteSalvato7 = clientiSalvati.get(6);
        Cliente clienteSalvato8 = clientiSalvati.get(7);
        Cliente clienteSalvato9 = clientiSalvati.get(8);
        Cliente clienteSalvato10 = clientiSalvati.get(9);

        // INDIRIZZI CLIENTE 1
        // Energia Calabria SRL

        Indirizzo cliente1SedeLegale = new Indirizzo(
                clienteSalvato1,
                TipoIndirizzo.SEDE_LEGALE,
                "Via del Progresso",
                "12",
                "88046",
                lameziaTerme
        );

        Indirizzo cliente1SedeOperativa = new Indirizzo(
                clienteSalvato1,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via delle Industrie",
                "35",
                "88100",
                catanzaro
        );

        // INDIRIZZI CLIENTE 2
        // Edil Sud SAS

        Indirizzo cliente2SedeLegale = new Indirizzo(
                clienteSalvato2,
                TipoIndirizzo.SEDE_LEGALE,
                "Via degli Artigiani",
                "8",
                "87100",
                cosenza
        );

        Indirizzo cliente2SedeOperativa = new Indirizzo(
                clienteSalvato2,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Contrada Industriale",
                "21",
                "88046",
                lameziaTerme
        );

        // INDIRIZZI CLIENTE 3
        // Tech Vision SPA

        Indirizzo cliente3SedeLegale = new Indirizzo(
                clienteSalvato3,
                TipoIndirizzo.SEDE_LEGALE,
                "Corso Giuseppe Mazzini",
                "104",
                "88100",
                catanzaro
        );

        Indirizzo cliente3SedeOperativa = new Indirizzo(
                clienteSalvato3,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dell'Innovazione",
                "18",
                "87100",
                cosenza
        );

        // INDIRIZZI CLIENTE 4
        // Comune di Lamezia Terme

        Indirizzo cliente4SedeLegale = new Indirizzo(
                clienteSalvato4,
                TipoIndirizzo.SEDE_LEGALE,
                "Via Senatore Arturo Perugini",
                "15",
                "88046",
                lameziaTerme
        );

        Indirizzo cliente4SedeOperativa = new Indirizzo(
                clienteSalvato4,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Corso Numistrano",
                "33",
                "88046",
                lameziaTerme
        );


        // INDIRIZZI CLIENTE 5
        // Beta Consulting SRL


        Indirizzo cliente5SedeLegale = new Indirizzo(
                clienteSalvato5,
                TipoIndirizzo.SEDE_LEGALE,
                "Via Vittorio Veneto",
                "42",
                "88900",
                crotone
        );

        Indirizzo cliente5SedeOperativa = new Indirizzo(
                clienteSalvato5,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dei Professionisti",
                "7",
                "88100",
                catanzaro
        );

        // INDIRIZZI CLIENTE 6
        // Gamma Energy SRL


        Indirizzo cliente6SedeLegale = new Indirizzo(
                clienteSalvato6,
                TipoIndirizzo.SEDE_LEGALE,
                "Via Roma",
                "56",
                "89900",
                viboValentia
        );

        Indirizzo cliente6SedeOperativa = new Indirizzo(
                clienteSalvato6,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dell'Energia",
                "29",
                "88046",
                lameziaTerme
        );

        // INDIRIZZI CLIENTE 7
        // Delta Costruzioni SPA

        Indirizzo cliente7SedeLegale = new Indirizzo(
                clienteSalvato7,
                TipoIndirizzo.SEDE_LEGALE,
                "Corso Garibaldi",
                "91",
                "89125",
                reggioCalabria
        );

        Indirizzo cliente7SedeOperativa = new Indirizzo(
                clienteSalvato7,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dei Cantieri",
                "14",
                "88900",
                crotone
        );


        // INDIRIZZI CLIENTE 8
        // Epsilon Informatica SRL

        Indirizzo cliente8SedeLegale = new Indirizzo(
                clienteSalvato8,
                TipoIndirizzo.SEDE_LEGALE,
                "Via Panebianco",
                "120",
                "87100",
                cosenza
        );

        Indirizzo cliente8SedeOperativa = new Indirizzo(
                clienteSalvato8,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via della Tecnologia",
                "44",
                "88100",
                catanzaro
        );

        // INDIRIZZI CLIENTE 9
        // Omega Service SAS

        Indirizzo cliente9SedeLegale = new Indirizzo(
                clienteSalvato9,
                TipoIndirizzo.SEDE_LEGALE,
                "Via XXV Aprile",
                "63",
                "88900",
                crotone
        );

        Indirizzo cliente9SedeOperativa = new Indirizzo(
                clienteSalvato9,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dei Servizi",
                "5",
                "89900",
                viboValentia
        );

        // INDIRIZZI CLIENTE 10
        // Studio Romano SRL

        Indirizzo cliente10SedeLegale = new Indirizzo(
                clienteSalvato10,
                TipoIndirizzo.SEDE_LEGALE,
                "Corso Giovanni Nicotera",
                "76",
                "88046",
                lameziaTerme
        );

        Indirizzo cliente10SedeOperativa = new Indirizzo(
                clienteSalvato10,
                TipoIndirizzo.SEDE_OPERATIVA,
                "Via dei Tecnici",
                "11",
                "89900",
                viboValentia
        );


        indirizzoRepository.saveAll(
                List.of(
                        cliente1SedeLegale,
                        cliente1SedeOperativa,

                        cliente2SedeLegale,
                        cliente2SedeOperativa,

                        cliente3SedeLegale,
                        cliente3SedeOperativa,

                        cliente4SedeLegale,
                        cliente4SedeOperativa,

                        cliente5SedeLegale,
                        cliente5SedeOperativa,

                        cliente6SedeLegale,
                        cliente6SedeOperativa,

                        cliente7SedeLegale,
                        cliente7SedeOperativa,

                        cliente8SedeLegale,
                        cliente8SedeOperativa,

                        cliente9SedeLegale,
                        cliente9SedeOperativa,

                        cliente10SedeLegale,
                        cliente10SedeOperativa
                )
        );

        System.out.println(
                "Seeder completato: inseriti 10 clienti e 20 indirizzi."
        );
    }

    private Comune trovaComune(String nome) {

        return comuneRepository
                .findByNome(nome)
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Impossibile eseguire il seeder: "
                                        + "il comune '"
                                        + nome
                                        + "' non è presente nel database."
                        )
                );
    }

    private Comune trovaComuneAlternativo(
            String nomePrincipale,
            String nomeAlternativo
    ) {

        return comuneRepository
                .findByNome(nomePrincipale)
                .or(
                        () -> comuneRepository.findByNome(
                                nomeAlternativo
                        )
                )
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Impossibile eseguire il seeder: "
                                        + "non è stato trovato né il comune '"
                                        + nomePrincipale
                                        + "' né il comune '"
                                        + nomeAlternativo
                                        + "'."
                        )
                );
    }
}