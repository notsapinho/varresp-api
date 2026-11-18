package br.com.fiap.varresp.controller;

import br.com.fiap.varresp.bo.DemandaSemanalBo;
import br.com.fiap.varresp.dto.DemandaAtualizacao;
import br.com.fiap.varresp.dto.DemandaRequest;
import br.com.fiap.varresp.dto.DemandaResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/demandas")
public class DemandaSemanalController {

    private final DemandaSemanalBo demandaBo;

    public DemandaSemanalController(DemandaSemanalBo demandaBo) {
        this.demandaBo = demandaBo;
    }

    @GetMapping
    public List<DemandaResponse> listar(@RequestParam(required = false) Long distritoId) {
        return demandaBo.listar(distritoId);
    }

    @GetMapping("/{distritoId}/{semana}")
    public DemandaResponse buscar(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {
        return demandaBo.buscar(distritoId, semana);
    }

    @PostMapping
    public ResponseEntity<DemandaResponse> criar(@Valid @RequestBody DemandaRequest request) {
        DemandaResponse criada = demandaBo.criar(request);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{distritoId}/{semana}")
                .buildAndExpand(criada.distritoId(), criada.semana())
                .toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{distritoId}/{semana}")
    public DemandaResponse atualizar(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana,
            @Valid @RequestBody DemandaAtualizacao request) {
        return demandaBo.atualizar(distritoId, semana, request);
    }

    @DeleteMapping("/{distritoId}/{semana}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {
        demandaBo.remover(distritoId, semana);
    }
}
