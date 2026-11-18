package br.com.fiap.varresp.dto;

import java.time.LocalDate;

public record DemandaResponse(Long distritoId, String distrito, LocalDate semana, Integer pedidos) {
}
