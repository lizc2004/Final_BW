package noemicoppotelli.finalbuildweek.payloads;

import noemicoppotelli.finalbuildweek.entities.Ruolo;

import java.util.Set;

public record UtenteResponseDTO(Long id,
                                String username,
                                String email,
                                String nome,
                                String cognome,
                                String avatar,
                                Set<Ruolo> ruoli) {
}
