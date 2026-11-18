package br.com.fiap.varresp.controller;

import br.com.fiap.varresp.bo.PrioridadeBo;
import br.com.fiap.varresp.dto.PrioridadeResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prioridades")
public class PrioridadeController {

    private final PrioridadeBo prioridadeBo;

    public PrioridadeController(PrioridadeBo prioridadeBo) {
        this.prioridadeBo = prioridadeBo;
    }

    @GetMapping
    public List<PrioridadeResponse> priorizar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana,
            @RequestParam(defaultValue = "3") int vagas) {
        return prioridadeBo.priorizar(semana, vagas);
    }
}
