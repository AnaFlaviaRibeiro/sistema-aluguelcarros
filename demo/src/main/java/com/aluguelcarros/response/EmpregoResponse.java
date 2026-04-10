package com.aluguelcarros.response;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
@Introspected
public record EmpregoResponse(Long id, String entidadeEmpregadora, BigDecimal renda) {
}
