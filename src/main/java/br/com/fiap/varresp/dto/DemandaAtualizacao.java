package br.com.fiap.varresp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record DemandaAtualizacao(
        @NotNull(message = "O número de pedidos é obrigatório")
        @PositiveOrZero(message = "O número de pedidos não pode ser negativo")
        @Max(value = 9999999, message = "O número de pedidos deve ter no máximo 7 dígitos")
        Integer pedidos) {
}
