package noemicoppotelli.finalbuildweek.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fattura")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal importo;

    @Column(nullable = false)
    private String numero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"indirizzi"})
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stato_fattura_id", nullable = false)
    private StatoFattura statoFattura;
}