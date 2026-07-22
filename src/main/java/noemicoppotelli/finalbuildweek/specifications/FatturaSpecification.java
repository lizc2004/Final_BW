package noemicoppotelli.finalbuildweek.specifications;

import noemicoppotelli.finalbuildweek.entities.Fattura;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FatturaSpecification {

    private FatturaSpecification() {
    }

    public static Specification<Fattura> hasCliente(Long clienteId) {
        return (root, query, cb) -> clienteId == null ? null : cb.equal(root.get("cliente").get("id"), clienteId);
    }

    public static Specification<Fattura> hasStato(Long statoFatturaId) {
        return (root, query, cb) -> statoFatturaId == null ? null : cb.equal(root.get("statoFattura").get("id"), statoFatturaId);
    }

    public static Specification<Fattura> hasData(LocalDate data) {
        return (root, query, cb) -> data == null ? null : cb.equal(root.get("data"), data);
    }

    public static Specification<Fattura> hasAnno(Integer anno) {
        return (root, query, cb) -> {
            if (anno == null) {
                return null;
            }
            LocalDate inizio = LocalDate.of(anno, 1, 1);
            LocalDate fine = LocalDate.of(anno, 12, 31);
            return cb.between(root.get("data"), inizio, fine);
        };
    }

    public static Specification<Fattura> importoBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min != null && max != null) {
                return cb.between(root.get("importo"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("importo"), min);
            }
            if (max != null) {
                return cb.lessThanOrEqualTo(root.get("importo"), max);
            }
            return null;
        };
    }
}
