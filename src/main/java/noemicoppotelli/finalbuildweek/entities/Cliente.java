package noemicoppotelli.finalbuildweek.entities;

import jakarta.persistence.*;
import lombok.*;
import noemicoppotelli.finalbuildweek.enums.TipoCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "ragione_sociale", nullable = false)
    private String ragioneSociale;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "partita_iva", unique = true, length = 11)
    private String partitaIva;

    @Column(name = "codice_fiscale", unique = true, length = 16)
    private String codiceFiscale;

    @Column(name = "data_inserimento", nullable = false)
    private LocalDateTime dataInserimento;

    @Column(name = "data_ultimo_contatto")
    private LocalDateTime dataUltimoContatto;

    @Column(name = "fatturato_annuale", precision = 15, scale = 2)
    private BigDecimal fatturatoAnnuale;

    @Column(unique = true)
    private String pec;

    @Column(unique = true)
    private String telefono;

    @Column(name = "email_contatto")
    private String emailContatto;

    @Column(name = "nome_contatto")
    private String nomeContatto;

    @Column(name = "cognome_contatto")
    private String cognomeContatto;

    @Column(name = "telefono_contatto")
    private String telefonoContatto;

    @Column(name = "logo_aziendale")
    private String logoAziendale;

    /*
     * Relazione inversa con gli indirizzi del cliente.
     *
     * mappedBy indica che la relazione è gestita dal campo
     * "cliente" presente nell'entity Indirizzo.
     *
     * @Builder.Default è necessario perché Cliente utilizza @Builder:
     * senza questa annotazione la lista potrebbe essere null
     * quando il cliente viene creato tramite builder.
     */
    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    @ToString.Exclude
    private List<Indirizzo> indirizzi = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;

    @PrePersist
    public void prePersist() {
        if (dataInserimento == null) {
            dataInserimento = LocalDateTime.now();
        }
    }
}