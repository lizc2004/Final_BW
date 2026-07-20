package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.NotBlank;

public record StatoFatturaPayloadDTO(
        @NotBlank(message = "Il nome dello stato è obbligatorio")
        String nome
) {
}
