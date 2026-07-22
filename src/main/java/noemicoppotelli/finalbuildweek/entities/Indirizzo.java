package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;

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
    @Setter
    private Cliente cliente;

    @Column(nullable = false)
    @Setter
    private TipoIndirizzo tipoIndirizzo;

    @Column(nullable = false)
    @Setter
    private String via;

    @Column(nullable = false)
    @Setter
    private String civico;

    @Column(nullable = false)
    @Setter
    private String cap;

    @Column(nullable = false)
    @Setter
    private String localita;

    @ManyToOne
    @JoinColumn(name = "comune_id")
    @Setter
    private Comune comune;


    public Indirizzo(Cliente cliente, TipoIndirizzo tipoIndirizzo,
                     String via, String civico, String cap, Comune comune) {
        this.cliente = cliente;
        this.tipoIndirizzo = tipoIndirizzo;
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.localita = comune.getNome();
        this.comune = comune;
    }
}
