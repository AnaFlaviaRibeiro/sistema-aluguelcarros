package com.aluguelcarros.response;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
@Introspected
public record ClientePerfilResponse(
        Long id,
        String nome,
        String email,
        String rg,
        String cpf,
        String endereco,
        String profissao,
        List<EmpregoResponse> empregos
) {
}
