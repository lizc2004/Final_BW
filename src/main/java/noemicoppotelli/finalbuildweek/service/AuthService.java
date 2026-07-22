package noemicoppotelli.finalbuildweek.service;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Ruolo;
import noemicoppotelli.finalbuildweek.entities.Utente;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.payloads.AuthResponseDTO;
import noemicoppotelli.finalbuildweek.payloads.LoginRequestDTO;
import noemicoppotelli.finalbuildweek.payloads.RegisterRequestDTO;
import noemicoppotelli.finalbuildweek.repositories.RuoloRepository;
import noemicoppotelli.finalbuildweek.repositories.UtenteRepository;
import noemicoppotelli.finalbuildweek.security.JWTTools;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final RuoloRepository ruoloRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTools jwtTools;

    public String register(RegisterRequestDTO request) {
        if (utenteRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username già in uso!");
        }
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email già in uso!");
        }
        
        String nomeRuolo = (request.getRuolo() != null && !request.getRuolo().isBlank())
                ? request.getRuolo()
                : "ROLE_USER";

        if (!nomeRuolo.startsWith("ROLE_")) {
            nomeRuolo = "ROLE_" + nomeRuolo.toUpperCase();
        }

        final String ruoloFinale = nomeRuolo;
        Ruolo ruoloAssegnato = ruoloRepository.findByNome(ruoloFinale)
                .orElseThrow(() -> new BadRequestException("Il ruolo selezionato non esiste!"));

        Utente utente = Utente.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nome(request.getNome())
                .cognome(request.getCognome())
                .ruoli(Set.of(ruoloAssegnato))
                .build();

        utenteRepository.save(utente);
        return "Utente registrato con successo con ruolo: " + ruoloFinale;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Utente utente = (Utente) authentication.getPrincipal();

        String token = jwtTools.createToken(utente);
        return new AuthResponseDTO(token);
    }
}