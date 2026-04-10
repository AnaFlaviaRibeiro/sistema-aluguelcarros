package com.aluguelcarros.security;

import com.aluguelcarros.model.type.TipoUsuario;

public record AuthPrincipal(Long userId, TipoUsuario tipo) {
    public static final String ATTRIBUTE = "authPrincipal";
}
