package noemicoppotelli.finalbuildweek.controllers;

import noemicoppotelli.finalbuildweek.entities.Comune;
import noemicoppotelli.finalbuildweek.service.ComuneService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comuni")
public class comuneController {
    private final ComuneService comuneService;

    public comuneController(ComuneService comuneService) {
        this.comuneService = comuneService;
    }

    // 1. GET
    // http://localhost:3001/comuni?page=0&size=5&orderBy=data
    // --> 200 OK ARRAY DI COMUNI

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<Comune> getComune(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "data") String orderBy) {

        return this.comuneService.getAll(page, size, orderBy);
    }

    // 2. GET
    // http://localhost:3001/comuni/{comuneId}
    // --> 200 OK COMUNE TROVATO

    @GetMapping("/{comuneId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Comune getById(
            @PathVariable Long comuneId) {

        return this.comuneService.findById(comuneId);
    }

    // 3. GET per nome provincia
    // http://localhost:3001/comuni/provincia/{provinciaNome}
    // --> 200 OK COMUNE TROVATO
    @GetMapping("/provincia/{provinciaNome}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Comune> findByProvincia(
            @PathVariable String provinciaNome) {
        return comuneService.findByProvinciaNome(provinciaNome);
    }

}
