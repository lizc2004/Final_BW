package noemicoppotelli.finalbuildweek.config;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.enums.TipoCliente;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Classe utilizzata per inserire automaticamente alcuni clienti di prova
 * all'avvio dell'applicazione.

 * I clienti vengono inseriti solamente quando la tabella "cliente" è vuota.
 * In questo modo, a ogni riavvio dell'applicazione, non vengono creati duplicati.
 */
@Component
@RequiredArgsConstructor
public class ClienteDataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    /*
     * Questo metodo viene eseguito automaticamente da Spring Boot
     * dopo l'avvio dell'applicazione.
     */
    @Override
    public void run(String... args) {

        /*
         * Se nella tabella esiste già almeno un cliente,
         * interrompiamo il seeder per evitare duplicati.
         */
        if (clienteRepository.count() > 0) {
            System.out.println(
                    "Seeder clienti non eseguito: nella tabella sono già presenti dei clienti."
            );
            return;
        }

        Cliente cliente1 = Cliente.builder()
                .ragioneSociale("Energia Calabria SRL")
                .email("info@energiacalabria.it")
                .partitaIva("01234567890")
                .codiceFiscale("01234567890")
                .dataInserimento(LocalDateTime.of(2026, 1, 10, 9, 0))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 15, 10, 30))
                .fatturatoAnnuale(new BigDecimal("150000.00"))
                .pec("pec@energiacalabria.it")
                .telefono("0968123456")
                .emailContatto("mario.rossi@energiacalabria.it")
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
                .dataInserimento(LocalDateTime.of(2025, 11, 22, 8, 30))
                .dataUltimoContatto(LocalDateTime.of(2026, 6, 30, 16, 15))
                .fatturatoAnnuale(new BigDecimal("82000.00"))
                .pec("pec@edilsud.it")
                .telefono("0984123456")
                .emailContatto("luca.bianchi@edilsud.it")
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
                .dataInserimento(LocalDateTime.of(2026, 3, 18, 9, 45))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 20, 11, 20))
                .fatturatoAnnuale(new BigDecimal("325000.00"))
                .pec("pec@techvision.it")
                .telefono("0965123456")
                .emailContatto("anna.verdi@techvision.it")
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
                .dataInserimento(LocalDateTime.of(2025, 8, 15, 8, 0))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 18, 9, 30))
                .fatturatoAnnuale(new BigDecimal("500000.00"))
                .pec("protocollo@pec.comune.it")
                .telefono("0968200000")
                .emailContatto("segreteria@comune.it")
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
                .dataInserimento(LocalDateTime.of(2026, 2, 5, 10, 15))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 2, 14, 40))
                .fatturatoAnnuale(new BigDecimal("97000.00"))
                .pec("pec@betaconsulting.it")
                .telefono("0967123456")
                .emailContatto("sara.russo@betaconsulting.it")
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
                .dataInserimento(LocalDateTime.of(2026, 4, 11, 11, 30))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 19, 17, 10))
                .fatturatoAnnuale(new BigDecimal("210000.00"))
                .pec("pec@gammaenergy.it")
                .telefono("0968123999")
                .emailContatto("giulia.gallo@gammaenergy.it")
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
                .dataInserimento(LocalDateTime.of(2026, 1, 28, 8, 45))
                .dataUltimoContatto(LocalDateTime.of(2026, 6, 28, 15, 0))
                .fatturatoAnnuale(new BigDecimal("180000.00"))
                .pec("pec@deltacostruzioni.it")
                .telefono("0985123456")
                .emailContatto("franco.esposito@deltacostruzioni.it")
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
                .dataInserimento(LocalDateTime.of(2026, 5, 3, 9, 20))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 21, 12, 0))
                .fatturatoAnnuale(new BigDecimal("125000.00"))
                .pec("pec@epsilon.it")
                .telefono("0969123456")
                .emailContatto("elena.greco@epsilon.it")
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
                .dataInserimento(LocalDateTime.of(2025, 12, 10, 13, 10))
                .dataUltimoContatto(LocalDateTime.of(2026, 5, 15, 10, 45))
                .fatturatoAnnuale(new BigDecimal("67000.00"))
                .pec("pec@omegaservice.it")
                .telefono("0962123456")
                .emailContatto("davide.marino@omegaservice.it")
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
                .dataInserimento(LocalDateTime.of(2026, 6, 1, 10, 0))
                .dataUltimoContatto(LocalDateTime.of(2026, 7, 22, 9, 15))
                .fatturatoAnnuale(new BigDecimal("56000.00"))
                .pec("pec@studioromano.it")
                .telefono("0968123888")
                .emailContatto("laura.romano@studioromano.it")
                .nomeContatto("Laura")
                .cognomeContatto("Romano")
                .telefonoContatto("3331234567")
                .tipoCliente(TipoCliente.SRL)
                .build();

        /*
         * Salviamo tutti i clienti con una sola operazione.
         */
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

        System.out.println("Seeder completato: inseriti 10 clienti di prova.");
    }
}