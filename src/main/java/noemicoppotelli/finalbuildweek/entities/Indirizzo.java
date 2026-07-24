package noemicoppotelli.finalbuildweek.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;

@Entity
@Table(name = "indirizzo")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Indirizzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_indirizzo", nullable = false)
    private TipoIndirizzo tipoIndirizzo;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private String civico;

    @Column(nullable = false, length = 5)
    private String cap;

    @Column(nullable = false)
    private String localita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comune_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Comune comune;

    public Indirizzo(
            Cliente cliente,
            TipoIndirizzo tipoIndirizzo,
            String via,
            String civico,
            String cap,
            Comune comune
    ) {
        this.cliente = cliente;
        this.tipoIndirizzo = tipoIndirizzo;
        this.via = via;
        this.civico = civico;
        this.cap = cap;
        this.comune = comune;
        this.localita = comune.getNome();
    }
}