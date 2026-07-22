package noemicoppotelli.finalbuildweek.specifications;

import noemicoppotelli.finalbuildweek.entities.Cliente;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * Questa classe contiene i filtri dinamici
 * utilizzabili per effettuare ricerche sui clienti.
 *
 * Ogni metodo restituisce una Specification<Cliente>,
 * cioè una condizione che verrà tradotta in SQL da JPA.
 */
public final class ClienteSpecification {

    /*
     * Costruttore privato.
     *
     * La classe contiene soltanto metodi statici,
     * quindi non deve essere istanziata.
     */
    private ClienteSpecification() {
    }

    /*
     * Cerca una parte della ragione sociale.
     *
     * La ricerca:
     * - non distingue maiuscole e minuscole;
     * - accetta anche una parte del nome;
     * - non applica alcun filtro se il valore è null o vuoto.
     *
     * Esempio:
     * "energia" trova "Energia Sud Srl".
     */
    public static Specification<Cliente> ragioneSocialeContiene(
            String ragioneSociale
    ) {

        return (root, query, criteriaBuilder) -> {

            if (ragioneSociale == null || ragioneSociale.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String valoreRicercato =
                    "%" + ragioneSociale.trim().toLowerCase() + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(
                            root.get("ragioneSociale")
                    ),
                    valoreRicercato
            );
        };
    }

    /*
     * Filtra i clienti con fatturato annuale
     * maggiore o uguale al valore indicato.
     */
    public static Specification<Cliente> fatturatoMaggioreUguale(
            BigDecimal fatturatoMin
    ) {

        return (root, query, criteriaBuilder) -> {

            if (fatturatoMin == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("fatturatoAnnuale"),
                    fatturatoMin
            );
        };
    }

    /*
     * Filtra i clienti con fatturato annuale
     * minore o uguale al valore indicato.
     */
    public static Specification<Cliente> fatturatoMinoreUguale(
            BigDecimal fatturatoMax
    ) {

        return (root, query, criteriaBuilder) -> {

            if (fatturatoMax == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("fatturatoAnnuale"),
                    fatturatoMax
            );
        };
    }

    /*
     * Filtra i clienti inseriti a partire
     * dalla data e ora indicate.
     */
    public static Specification<Cliente> dataInserimentoDa(
            LocalDateTime dataInserimentoDa
    ) {

        return (root, query, criteriaBuilder) -> {

            if (dataInserimentoDa == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dataInserimento"),
                    dataInserimentoDa
            );
        };
    }

    /*
     * Filtra i clienti inseriti entro
     * la data e ora indicate.
     */
    public static Specification<Cliente> dataInserimentoA(
            LocalDateTime dataInserimentoA
    ) {

        return (root, query, criteriaBuilder) -> {

            if (dataInserimentoA == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("dataInserimento"),
                    dataInserimentoA
            );
        };
    }

    /*
     * Filtra i clienti il cui ultimo contatto
     * è avvenuto a partire dalla data indicata.
     */
    public static Specification<Cliente> dataUltimoContattoDa(
            LocalDateTime dataUltimoContattoDa
    ) {

        return (root, query, criteriaBuilder) -> {

            if (dataUltimoContattoDa == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dataUltimoContatto"),
                    dataUltimoContattoDa
            );
        };
    }

    /*
     * Filtra i clienti il cui ultimo contatto
     * è avvenuto entro la data indicata.
     */
    public static Specification<Cliente> dataUltimoContattoA(
            LocalDateTime dataUltimoContattoA
    ) {

        return (root, query, criteriaBuilder) -> {

            if (dataUltimoContattoA == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("dataUltimoContatto"),
                    dataUltimoContattoA
            );
        };
    }
}