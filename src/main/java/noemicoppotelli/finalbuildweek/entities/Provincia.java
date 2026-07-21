package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Setter
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sigla;

    @Column(nullable = false)
    private String regione;

    public Provincia(String name, String sigla, String regione) {
        this.name = name;
        this.sigla = sigla;
        this.regione = regione;
    }
}
