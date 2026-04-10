package com.aluguelcarros.controller;

import com.aluguelcarros.request.LoginRequest;
import com.aluguelcarros.request.RegistroClienteRequest;
import com.aluguelcarros.response.ClientePerfilResponse;
import com.aluguelcarros.response.LoginResponse;
import com.aluguelcarros.service.AuthService;
import com.aluguelcarros.service.ClienteService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;

import java.net.URI;

@Controller("/auth")
public class AuthController {

    private final AuthService authService;
    private final ClienteService clienteService;

    public AuthController(AuthService authService, ClienteService clienteService) {
        this.authService = authService;
        this.clienteService = clienteService;
    }

    @Post("/login")
    public LoginResponse login(@Body @Valid LoginRequest request) {
        return authService.login(request);
    }

    @Post("/registro/cliente")
    public HttpResponse<ClientePerfilResponse> registrarCliente(@Body @Valid RegistroClienteRequest request) {
        ClientePerfilResponse body = clienteService.registrar(request);
        return HttpResponse.created(body)
                .headers(h -> h.location(URI.create("/cliente/perfil")));
    }
}
