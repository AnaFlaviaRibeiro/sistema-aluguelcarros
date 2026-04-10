package com.aluguelcarros.request;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Serdeable
@Introspected
public class EmpregoRequest {

    @NotBlank
    private String entidadeEmpregadora;

    @NotNull
    @DecimalMin(value = "0.01", message = "Renda deve ser maior que zero.")
    private BigDecimal renda;

    public String getEntidadeEmpregadora() {
        return entidadeEmpregadora;
    }

    public BigDecimal getRenda() {
        return renda;
    }

    public void setEntidadeEmpregadora(String entidadeEmpregadora) {
        this.entidadeEmpregadora = entidadeEmpregadora;
    }

    public void setRenda(BigDecimal renda) {
        this.renda = renda;
    }
}
