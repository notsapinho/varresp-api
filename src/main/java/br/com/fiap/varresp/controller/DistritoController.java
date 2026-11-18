package br.com.fiap.varresp.controller;

import br.com.fiap.varresp.bo.DistritoBo;
import br.com.fiap.varresp.dto.DistritoRequest;
import br.com.fiap.varresp.dto.DistritoResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/distritos")
public class DistritoController {

    private final DistritoBo distritoBo;

    public DistritoController(DistritoBo distritoBo) {
        this.distritoBo = distritoBo;
    }

    @GetMapping
    public List<DistritoResponse> listar() {
        return distritoBo.listar();
    }

    @GetMapping("/{id}")
    public DistritoResponse buscar(@PathVariable Long id) {
        return distritoBo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<DistritoResponse> criar(@Valid @RequestBody DistritoRequest request) {
        DistritoResponse criado = distritoBo.criar(request);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    public DistritoResponse atualizar(@PathVariable Long id, @Valid @RequestBody DistritoRequest request) {
        return distritoBo.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        distritoBo.remover(id);
    }
}
