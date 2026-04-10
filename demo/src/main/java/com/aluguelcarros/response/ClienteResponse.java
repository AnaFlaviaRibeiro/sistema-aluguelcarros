package com.aluguelcarros.response;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String rg,
        String cpf,
        String endereco,
        String profissao
) {
}
