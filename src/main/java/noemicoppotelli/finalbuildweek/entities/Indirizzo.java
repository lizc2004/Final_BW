package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private TipoIndirizzo tipoIndirizzo;

    private String via;

    private int civico;

    private int cap;

    private String localita;

    private Comune comune;
}
