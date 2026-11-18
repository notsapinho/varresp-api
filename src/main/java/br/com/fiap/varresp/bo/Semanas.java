package br.com.fiap.varresp.bo;

import br.com.fiap.varresp.exception.RequisicaoInvalidaException;
import java.time.DayOfWeek;
import java.time.LocalDate;

final class Semanas {

    private Semanas() {
    }

    static void exigirSegundaFeira(LocalDate semana) {
        if (semana.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new RequisicaoInvalidaException("A semana deve ser informada pela segunda-feira que a inicia");
        }
    }
}
