package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import noemicoppotelli.finalbuildweek.entities.Indirizzo;
import noemicoppotelli.finalbuildweek.exceptions.ValidationException;
import noemicoppotelli.finalbuildweek.payloads.IndirizzoDTO;
import noemicoppotelli.finalbuildweek.service.IndirizzoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indirizzi")
public class IndirizzoController {
    private final IndirizzoService indirizzoService;

    public IndirizzoController(IndirizzoService indirizzoService) {
        this.indirizzoService = indirizzoService;
    }

    // 1. GET ALL
    // http://localhost:3001/Indirizzi?page=0&size=5&orderBy=data
    // --> 200 OK ARRAY DI INDIRIZZI
    @GetMapping
    public Page<Indirizzo> getAll(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "data") String orderBy) {
        return this.indirizzoService.getAll(page, size, orderBy);
    }


    // 3. GET
    // http://localhost:3001/indirizzo/{indirizzoId}
    // --> 200 OK EVENTO TROVATO
    @GetMapping("/{indirizzoId}")
    public Indirizzo findById(@PathVariable Long id) {

        return indirizzoService.findById(id);
    }

    // 4. PUT
    // http://localhost:3001/indirizzi/{indirizzoId}
    // + payload
    // --> 200 OK INDIRIZZO AGGIORNATO

    @PutMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Indirizzo update(
            @PathVariable Long indirizzoId,
            @Valid @RequestBody IndirizzoDTO body,
            BindingResult validation) {

        if (validation.hasErrors()) {

            List<String> errorsList = validation
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        }

        return this.indirizzoService.update(indirizzoId, body);
    }


    // 5. DELETE
    // http://localhost:3001/indirizzi/{indirizzoId}
    // --> 204 NO CONTENT

    @DeleteMapping("/{indirizzoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(
            @PathVariable Long indirizzoId) {

        this.indirizzoService.delete(indirizzoId);
    }
}
