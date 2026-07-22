package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.StatoFattura;
import noemicoppotelli.finalbuildweek.exceptions.ValidationException;
import noemicoppotelli.finalbuildweek.payloads.StatoFatturaPayloadDTO;
import noemicoppotelli.finalbuildweek.service.StatoFatturaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stati-fattura")
@RequiredArgsConstructor
public class StatoFatturaController {

    private final StatoFatturaService statoFatturaService;

    @GetMapping
    public List<StatoFattura> findAll() {
        return statoFatturaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public StatoFattura create(@RequestBody @Valid StatoFatturaPayloadDTO payload, BindingResult bindingResult) {
        checkValidation(bindingResult);
        return statoFatturaService.save(payload);
    }

    private void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.toList());
            throw new ValidationException(errors);
        }
    }
}
