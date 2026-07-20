package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String regione;

    public Provincia(String name, String sigla, String regione) {
        this.name = name;
        this.sigla = sigla;
        this.regione = regione;
    }
}
