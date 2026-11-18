package br.com.fiap.varresp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PrevisaoResponse(
        Long distritoId,
        String distrito,
        LocalDate semana,
        BigDecimal volumePrevisto,
        BigDecimal probAlta,
        Integer limiteAlta) {
}
