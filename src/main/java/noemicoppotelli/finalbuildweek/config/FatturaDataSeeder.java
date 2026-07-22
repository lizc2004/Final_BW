package noemicoppotelli.finalbuildweek.config;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Cliente;
import noemicoppotelli.finalbuildweek.entities.Fattura;
import noemicoppotelli.finalbuildweek.entities.StatoFattura;
import noemicoppotelli.finalbuildweek.repositories.ClienteRepository;
import noemicoppotelli.finalbuildweek.repositories.FatturaRepository;
import noemicoppotelli.finalbuildweek.repositories.StatoFatturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(6)
public class FatturaDataSeeder implements CommandLineRunner {

    private final FatturaRepository fatturaRepository;
    private final ClienteRepository clienteRepository;
    private final StatoFatturaRepository statoFatturaRepository;

    @Override
    public void run(String... args) {

        if (fatturaRepository.count() > 0) return;

        List<Cliente> clienti = clienteRepository.findAll();

        if (clienti.size() < 10) {
            System.out.println("Impossibile creare le fatture: clienti insufficienti.");
            return;
        }

        StatoFattura pagata = statoFatturaRepository.findByNomeIgnoreCase("PAGATA")
                .orElseThrow();

        StatoFattura emessa = statoFatturaRepository.findByNomeIgnoreCase("EMESSA")
                .orElseThrow();

        StatoFattura inviata = statoFatturaRepository.findByNomeIgnoreCase("INVIATA")
                .orElseThrow();

        List<Fattura> fatture = List.of(

                Fattura.builder()
                        .numero("FT-2026-001")
                        .data(LocalDate.of(2026, 1, 10))
                        .importo(new BigDecimal("1250.00"))
                        .cliente(clienti.get(0))
                        .statoFattura(pagata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-002")
                        .data(LocalDate.of(2026, 2, 4))
                        .importo(new BigDecimal("890.50"))
                        .cliente(clienti.get(1))
                        .statoFattura(emessa)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-003")
                        .data(LocalDate.of(2026, 2, 21))
                        .importo(new BigDecimal("1560.00"))
                        .cliente(clienti.get(2))
                        .statoFattura(inviata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-004")
                        .data(LocalDate.of(2026, 3, 8))
                        .importo(new BigDecimal("430.00"))
                        .cliente(clienti.get(3))
                        .statoFattura(pagata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-005")
                        .data(LocalDate.of(2026, 3, 18))
                        .importo(new BigDecimal("2790.90"))
                        .cliente(clienti.get(4))
                        .statoFattura(emessa)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-006")
                        .data(LocalDate.of(2026, 4, 5))
                        .importo(new BigDecimal("980.00"))
                        .cliente(clienti.get(5))
                        .statoFattura(inviata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-007")
                        .data(LocalDate.of(2026, 4, 27))
                        .importo(new BigDecimal("3100.00"))
                        .cliente(clienti.get(6))
                        .statoFattura(pagata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-008")
                        .data(LocalDate.of(2026, 5, 14))
                        .importo(new BigDecimal("720.40"))
                        .cliente(clienti.get(7))
                        .statoFattura(emessa)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-009")
                        .data(LocalDate.of(2026, 6, 2))
                        .importo(new BigDecimal("5400.00"))
                        .cliente(clienti.get(8))
                        .statoFattura(pagata)
                        .build(),

                Fattura.builder()
                        .numero("FT-2026-010")
                        .data(LocalDate.of(2026, 6, 28))
                        .importo(new BigDecimal("1150.75"))
                        .cliente(clienti.get(9))
                        .statoFattura(inviata)
                        .build()

        );

        fatturaRepository.saveAll(fatture);

        System.out.println("Sono state create 10 fatture di esempio.");
    }
}