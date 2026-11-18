package br.com.fiap.varresp.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PrevisaoAtualizacao(
        @NotNull(message = "O volume previsto é obrigatório")
        @DecimalMin(value = "0.0", message = "O volume previsto não pode ser negativo")
        @Digits(integer = 7, fraction = 3, message = "O volume previsto aceita até 7 dígitos inteiros e 3 decimais")
        BigDecimal volumePrevisto,
        @NotNull(message = "A probabilidade de alta é obrigatória")
        @DecimalMin(value = "0.0", message = "A probabilidade deve estar entre 0 e 1")
        @DecimalMax(value = "1.0", message = "A probabilidade deve estar entre 0 e 1")
        @Digits(integer = 1, fraction = 6, message = "A probabilidade aceita até 6 casas decimais")
        BigDecimal probAlta,
        @NotNull(message = "O limite de alta é obrigatório")
        @Positive(message = "O limite de alta deve ser maior que zero")
        @Max(value = 9999999, message = "O limite de alta deve ter no máximo 7 dígitos")
        Integer limiteAlta) {
}
