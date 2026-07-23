package noemicoppotelli.finalbuildweek.controllers;


import noemicoppotelli.finalbuildweek.entities.Provincia;
import noemicoppotelli.finalbuildweek.service.ProvinciaService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/province")
public class ProvinciaController {
    private final ProvinciaService provinciaService;

    public ProvinciaController(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }


    // 1. GET
    // http://localhost:3001/provincie?page=0&size=5&orderBy=data
    // --> 200 OK ARRAY DI PROVINCE

    @GetMapping
    public Page<Provincia> getProvincia(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "regione") String orderBy) {

        return this.provinciaService.getAll(page, size, orderBy);
    }

    // 2. GET
    // http://localhost:3001/provincie/{provinciaId}
    // --> 200 OK PROVINCIA TROVATO

    @GetMapping("/{provinciaId}")
    public Provincia getById(
            @PathVariable Long provinciaId) {

        return this.provinciaService.findById(provinciaId);
    }

    // 3. GET per sigla
    // http://localhost:3001/provincie/sigla/{sigla}
    // --> 200 OK PROVINCIA TROVATO
    @GetMapping("/sigla/{sigla}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Provincia findBySigla(
            @PathVariable String sigla) {

        return provinciaService.findBySigla(sigla);
    }


    // 4. GET per nome
    // http://localhost:3001/provincie/nome/{nome}
    // --> 200 OK PROVINCIA TROVATO
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Provincia findByNome(
            @PathVariable String nome) {
        return provinciaService.findByName(nome);
    }
}
