package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Utente;
import noemicoppotelli.finalbuildweek.exceptions.BadRequestException;
import noemicoppotelli.finalbuildweek.payloads.*;
import noemicoppotelli.finalbuildweek.service.UtenteService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;


    //GET /utenti/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UtenteResponseDTO findById(
            @PathVariable Long id
    ) {
        Utente found = utenteService.getById(id);
        return new UtenteResponseDTO(found.getId(), found.getUsername(),
                found.getEmail(), found.getNome(), found.getCognome(),
                found.getAvatar(), found.getRuoli());
    }

    @GetMapping
    public Page<Utente> getAllUtenti(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(required = false) String nome) {
        return this.utenteService.getAllPerNome(page, size, nome);
    }


    // PUT /utenti/me

    @PutMapping("/me")
    public Utente updateMe(
            @AuthenticationPrincipal Utente utente,
            @Valid @RequestBody UtenteUpdateDTO body
    ) throws BadRequestException {
        return this.utenteService.update(utente.getId(), body);
    }

    // PUT /utenti/me/password
    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal Utente utente,
            @Valid @RequestBody PasswordChangeDTO body
    ) throws BadRequestException {

        utenteService.changePassword(
                utente.getId(),
                body
        );
    }

    // DELETE /utenti/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {

        utenteService.delete(id);

    }

    // POST /utenti/{utenteId}/ruoli/{ruoloId}
    @PostMapping("/{utenteId}/ruoli/{ruoloId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Utente addRole(
            @PathVariable Long utenteId,
            @PathVariable Long ruoloId
    ) throws BadRequestException {

        return utenteService.addRole(
                utenteId,
                ruoloId
        );

    }

    //DELETE /utenti/{utenteId}/ruoli/{ruoloId}
    @DeleteMapping("/{utenteId}/ruoli/{ruoloId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Utente removeRole(
            @PathVariable Long utenteId,
            @PathVariable Long ruoloId
    ) throws BadRequestException {

        return utenteService.removeRole(utenteId, ruoloId);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Utente getMyProfile(
            @AuthenticationPrincipal Utente utente
    ) {
        return utente;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UtenteDTO save(
            @Valid @RequestBody RegisterRequestDTO body
    ) {
        Utente utente = utenteService.save(body);
        return new UtenteDTO(utente.getUsername(),
                utente.getEmail(),
                utente.getNome(),
                utente.getCognome(),
                utente.getAvatar(),
                utente.getRuoli().toString());
    }
}
