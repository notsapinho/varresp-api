package br.com.fiap.varresp.controller;

import br.com.fiap.varresp.bo.PrevisaoSemanalBo;
import br.com.fiap.varresp.dto.PrevisaoAtualizacao;
import br.com.fiap.varresp.dto.PrevisaoRequest;
import br.com.fiap.varresp.dto.PrevisaoResponse;
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
@RequestMapping("/api/previsoes")
public class PrevisaoSemanalController {

    private final PrevisaoSemanalBo previsaoBo;

    public PrevisaoSemanalController(PrevisaoSemanalBo previsaoBo) {
        this.previsaoBo = previsaoBo;
    }

    @GetMapping
    public List<PrevisaoResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {
        return previsaoBo.listar(semana);
    }

    @GetMapping("/{distritoId}/{semana}")
    public PrevisaoResponse buscar(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {
        return previsaoBo.buscar(distritoId, semana);
    }

    @PostMapping
    public ResponseEntity<PrevisaoResponse> criar(@Valid @RequestBody PrevisaoRequest request) {
        PrevisaoResponse criada = previsaoBo.criar(request);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{distritoId}/{semana}")
                .buildAndExpand(criada.distritoId(), criada.semana())
                .toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{distritoId}/{semana}")
    public PrevisaoResponse atualizar(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana,
            @Valid @RequestBody PrevisaoAtualizacao request) {
        return previsaoBo.atualizar(distritoId, semana, request);
    }

    @DeleteMapping("/{distritoId}/{semana}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @PathVariable Long distritoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {
        previsaoBo.remover(distritoId, semana);
    }
}
