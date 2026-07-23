package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.NotBlank;

public record EmailRequestDTO(
        @NotBlank(message = "L'oggetto dell'email è obbligatorio")
        String oggetto,

        @NotBlank(message = "Il testo del messaggio è obbligatorio")
        String messaggio
) {
}