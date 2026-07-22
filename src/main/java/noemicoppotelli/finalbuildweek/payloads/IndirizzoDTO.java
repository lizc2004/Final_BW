package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import noemicoppotelli.finalbuildweek.enums.TipoIndirizzo;

public record IndirizzoDTO(
        @NotNull(message = "L'indirizzo è obbligatorio")
        @Size(max = 150)
        String via,
        @NotNull(message = "il civico è obbligatorio")
        @Size(max = 20)
        String civico,
        @NotNull(message = "La località è obbligatoria")
        String localita,
        @NotNull(message = "il CAP è obbligatorio")
        @Pattern(regexp = "\\d{5}")
        String cap,
        @NotNull(message = "inserire un nome valido")
        String nomeComune,
        @NotNull(message = "inserire un tipo di indirizzo valido")
        TipoIndirizzo tipoIndirizzo) {
}
