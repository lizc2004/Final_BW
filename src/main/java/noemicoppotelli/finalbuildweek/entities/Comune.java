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
public class Comune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String codiceProvincia;

    @Column(nullable = false)
    private String progressivoComune;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    public Comune(String nome, String codiceProvincia, Provincia provincia) {
        this.nome = nome;
        this.codiceProvincia = codiceProvincia;
        this.provincia = provincia;
    }


}
