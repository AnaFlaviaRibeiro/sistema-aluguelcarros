package com.aluguelcarros.response;

import com.aluguelcarros.model.type.TipoUsuario;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record LoginResponse(
        String token,
        TipoUsuario tipo,
        Long userId,
        String nome,
        String tipoAgente
) {
}
