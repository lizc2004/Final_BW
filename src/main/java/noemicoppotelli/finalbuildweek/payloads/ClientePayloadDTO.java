package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import noemicoppotelli.finalbuildweek.enums.TipoCliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClientePayloadDTO(

        @NotBlank(message = "La ragione sociale è obbligatoria")
        String ragioneSociale,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email non è valida")
        String email,

        @NotBlank(message = "La partita IVA è obbligatoria")
        String partitaIva,

        @NotBlank(message = "Il codice fiscale è obbligatorio")
        String codiceFiscale,

        LocalDateTime dataUltimoContatto,

        @PositiveOrZero(message = "Il fatturato annuale non può essere negativo")
        BigDecimal fatturatoAnnuale,

        @Email(message = "La PEC non è valida")
        String pec,

        String telefono,

        @Email(message = "L'email di contatto non è valida")
        String emailContatto,

        String nomeContatto,

        String cognomeContatto,

        String telefonoContatto,

        @NotNull(message = "Il tipo cliente è obbligatorio")
        TipoCliente tipoCliente
) {
}