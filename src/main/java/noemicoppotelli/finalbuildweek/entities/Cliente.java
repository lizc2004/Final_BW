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

    /*
     * Codice fiscale del cliente.
     *
     * Il valore deve essere univoco e può contenere
     * al massimo 16 caratteri.
     */
    @Column(name = "codice_fiscale", unique = true, length = 16)
    private String codiceFiscale;

    /*
     * Data e ora in cui il cliente è stato inserito
     * nel sistema.
     *
     * Il campo è obbligatorio.
     */
    @Column(name = "data_inserimento", nullable = false)
    private LocalDateTime dataInserimento;

    /*
     * Data e ora dell'ultimo contatto avuto con il cliente.
     *
     * Può essere null quando non è ancora avvenuto
     * alcun contatto.
     */
    @Column(name = "data_ultimo_contatto")
    private LocalDateTime dataUltimoContatto;

    /*
     * Fatturato annuale del cliente.
     *
     * precision = 15 indica il numero massimo totale
     * di cifre.
     *
     * scale = 2 indica il numero di cifre decimali.
     */
    @Column(name = "fatturato_annuale", precision = 15, scale = 2)
    private BigDecimal fatturatoAnnuale;

    /*
     * Indirizzo PEC del cliente.
     *
     * Il valore deve essere univoco.
     */
    @Column(unique = true)
    private String pec;

    /*
     * Numero di telefono principale del cliente.
     *
     * Il valore deve essere univoco.
     */
    @Column(unique = true)
    private String telefono;

    /*
     * Email della persona di riferimento all'interno
     * dell'azienda cliente.
     */
    @Column(name = "email_contatto")
    private String emailContatto;

    /*
     * Nome della persona di riferimento.
     */
    @Column(name = "nome_contatto")
    private String nomeContatto;

    /*
     * Cognome della persona di riferimento.
     */
    @Column(name = "cognome_contatto")
    private String cognomeContatto;

    /*
     * Numero di telefono della persona di riferimento.
     */
    @Column(name = "telefono_contatto")
    private String telefonoContatto;

    /*
     * Percorso, nome file o URL del logo aziendale.
     */
    @Column(name = "logo_aziendale")
    private String logoAziendale;

    /*
     * Relazione uno-a-molti tra Cliente e Indirizzo.
     *
     * Un cliente può possedere più indirizzi, mentre
     * ogni indirizzo appartiene a un solo cliente.
     *
     * mappedBy = "cliente" indica che la relazione viene
     * gestita dal campo cliente presente nell'entity Indirizzo.
     *
     * cascade = CascadeType.ALL indica che le operazioni
     * effettuate sul cliente vengono propagate agli indirizzi.
     *
     * Esempio:
     * salvando un cliente vengono salvati anche i suoi indirizzi.
     *
     * orphanRemoval = true indica che, quando un indirizzo
     * viene rimosso dalla lista, viene eliminato anche
     * dal database.
     *
     * @Builder.Default garantisce che la lista venga
     * inizializzata anche quando il cliente viene creato
     * tramite Cliente.builder().
     *
     * @ToString.Exclude evita una ricorsione infinita tra:
     * Cliente -> Indirizzo -> Cliente.
     */
    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    @ToString.Exclude
    private List<Indirizzo> indirizzi = new ArrayList<>();

    /*
     * Tipologia giuridica del cliente.
     *
     * EnumType.STRING consente di salvare nel database
     * il nome dell'enum, per esempio:
     *
     * SRL
     * SPA
     * SAS
     * PA
     *
     * invece della posizione numerica dell'enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;

    /*
     * Aggiunge un indirizzo al cliente rispettando
     * le regole di dominio.
     *
     * Un cliente può avere al massimo due indirizzi:
     *
     * - una sede legale;
     * - una sede operativa.
     *
     * Non è possibile inserire due indirizzi dello
     * stesso tipo.
     *
     * Il metodo sincronizza entrambi i lati della relazione:
     *
     * - aggiunge l'indirizzo alla lista del cliente;
     * - assegna il cliente all'indirizzo.
     */
    public void aggiungiIndirizzo(Indirizzo indirizzo) {

        /*
         * Evitiamo di aggiungere un riferimento nullo
         * alla lista degli indirizzi.
         */
        if (indirizzo == null) {
            throw new IllegalArgumentException(
                    "L'indirizzo non può essere nullo."
            );
        }

        /*
         * Verifichiamo che il cliente non abbia già
         * raggiunto il limite massimo di due indirizzi.
         */
        if (indirizzi.size() >= 2) {
            throw new IllegalStateException(
                    "Un cliente può avere al massimo due indirizzi."
            );
        }

        /*
         * Controlliamo se nella lista è già presente
         * un indirizzo dello stesso tipo.
         *
         * Per esempio, se il cliente possiede già una
         * SEDE_LEGALE, non può aggiungerne un'altra.
         */
        boolean tipoGiaPresente = indirizzi.stream()
                .anyMatch(indirizzoEsistente ->
                        indirizzoEsistente.getTipoIndirizzo()
                                == indirizzo.getTipoIndirizzo()
                );

        /*
         * Se il tipo è già presente, blocchiamo
         * l'inserimento.
         */
        if (tipoGiaPresente) {
            throw new IllegalStateException(
                    "Il cliente possiede già un indirizzo di tipo "
                            + indirizzo.getTipoIndirizzo()
            );
        }

        /*
         * Aggiungiamo l'indirizzo alla collezione del cliente.
         */
        indirizzi.add(indirizzo);

        /*
         * Impostiamo il cliente anche all'interno
         * dell'indirizzo, mantenendo sincronizzati
         * entrambi i lati della relazione.
         */
        indirizzo.setCliente(this);
    }

    /*
     * Rimuove un indirizzo dalla lista del cliente.
     *
     * Grazie a orphanRemoval = true, quando il cliente
     * viene salvato l'indirizzo rimosso dalla lista
     * viene eliminato anche dal database.
     */
    public void rimuoviIndirizzo(Indirizzo indirizzo) {

        /*
         * Se l'indirizzo è nullo non è necessario
         * eseguire alcuna operazione.
         */
        if (indirizzo == null) {
            return;
        }

        /*
         * Rimuoviamo l'indirizzo dalla lista.
         */
        indirizzi.remove(indirizzo);

        /*
         * Rimuoviamo anche il riferimento al cliente
         * presente nell'indirizzo.
         */
        indirizzo.setCliente(null);
    }

    /*
     * Metodo eseguito automaticamente da JPA
     * prima del primo salvataggio dell'entity.
     *
     * Se dataInserimento non è stata specificata,
     * viene assegnata la data e l'ora attuali.
     */
    @PrePersist
    public void prePersist() {

        if (dataInserimento == null) {
            dataInserimento = LocalDateTime.now();
        }
    }
}