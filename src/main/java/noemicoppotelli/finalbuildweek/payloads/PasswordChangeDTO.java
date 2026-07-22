package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeDTO(@NotBlank(message = "La password attuale è obbligatoria")
                                String oldPassword,


                                @NotBlank(message = "La nuova password è obbligatoria")
                                @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
                                String newPassword) {
}
