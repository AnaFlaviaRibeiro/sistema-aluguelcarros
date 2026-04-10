package com.aluguelcarros.controller;
import com.aluguelcarros.request.ClienteRequest;
import com.aluguelcarros.response.ClienteResponse;
import com.aluguelcarros.service.ClienteService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@Controller("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Post
    public HttpResponse<ClienteResponse> criar(@Body @Valid ClienteRequest request) {
        ClienteResponse response = clienteService.salvar(request);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/clientes/" + response.id())));
    }

    @Get
    public List<ClienteResponse> listar() {
        return clienteService.listarTodos();
    }

    @Get("/{id}")
    public ClienteResponse buscarPorId(Long id) {
        return clienteService.buscarPorId(id);
    }

    @Put("/{id}")
    public ClienteResponse atualizar(Long id, @Body @Valid ClienteRequest request) {
        return clienteService.atualizar(id, request);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(Long id) {
        clienteService.deletar(id);
        return HttpResponse.noContent();
    }
}