package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.NotNull;

public record RuoloUtenteDTO(@NotNull(message = "L'id del ruolo è obbligatorio")
                             Long ruoloId) {
}
