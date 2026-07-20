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

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private TipoIndirizzo tipoIndirizzo;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private int civico;

    @Column(nullable = false)
    private int cap;

    private String localita;

    @ManyToOne
    @JoinColumn(name = "comune_id")
    private Comune comune;


    private Indirizzo(Cliente cliente, TipoIndirizzo tipoIndirizzo,
                      String via, int civico, int cap, Comune comune) {
        this.cliente = cliente;
        this.tipoIndirizzo = tipoIndirizzo;
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.localita = comune.getNome();
        this.comune = comune;
    }
}
