package noemicoppotelli.finalbuildweek.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.entities.Fattura;
import noemicoppotelli.finalbuildweek.exceptions.ValidationException;
import noemicoppotelli.finalbuildweek.payloads.FatturaPayloadDTO;
import noemicoppotelli.finalbuildweek.service.FatturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
public class FatturaController {

    private final FatturaService fatturaService;

    @GetMapping
    public Page<Fattura> findAll(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long statoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) BigDecimal importoMin,
            @RequestParam(required = false) BigDecimal importoMax,
            @PageableDefault(size = 20, sort = "data") Pageable pageable
    ) {
        return fatturaService.findAll(clienteId, statoId, data, anno, importoMin, importoMax, pageable);
    }

    @GetMapping("/{id}")
    public Fattura findById(@PathVariable Long id) {
        return fatturaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Fattura create(@RequestBody @Valid FatturaPayloadDTO payload, BindingResult bindingResult) {
        checkValidation(bindingResult);
        return fatturaService.save(payload);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Fattura update(@PathVariable Long id, @RequestBody @Valid FatturaPayloadDTO payload, BindingResult bindingResult) {
        checkValidation(bindingResult);
        return fatturaService.update(id, payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        fatturaService.delete(id);
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
