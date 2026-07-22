package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stato_fattura")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StatoFattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public StatoFattura(String nome) {
        this.nome = nome;
    }
}
