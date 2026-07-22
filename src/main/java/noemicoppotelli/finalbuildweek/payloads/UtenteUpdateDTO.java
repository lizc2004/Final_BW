package noemicoppotelli.finalbuildweek.payloads;

import jakarta.validation.constraints.Email;

public record UtenteUpdateDTO(@Email(message = "Email non valida")
                              String email,

                              String nome,

                              String cognome,

                              String avatar) {
}
