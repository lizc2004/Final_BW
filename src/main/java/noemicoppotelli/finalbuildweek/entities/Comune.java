package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String codice_catastale;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    public Comune(String nome, String codice_catastale, Provincia provincia) {
        this.nome = nome;
        this.codice_catastale = codice_catastale;
        this.provincia = provincia;
    }
}
