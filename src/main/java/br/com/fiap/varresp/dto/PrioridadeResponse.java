package br.com.fiap.varresp.dto;

import java.math.BigDecimal;

public record PrioridadeResponse(
        int posicao,
        Long distritoId,
        String distrito,
        BigDecimal probAlta,
        BigDecimal volumePrevisto) {
}
