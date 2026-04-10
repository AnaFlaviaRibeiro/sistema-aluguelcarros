package com.aluguelcarros.controller;

import com.aluguelcarros.model.type.StatusPedido;
import com.aluguelcarros.request.PedidoAluguelRequest;
import com.aluguelcarros.response.PedidoAluguelResponse;
import com.aluguelcarros.service.PedidoAluguelService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@Controller("/pedidos")
public class PedidoAluguelController {

    private final PedidoAluguelService pedidoService;

    public PedidoAluguelController(PedidoAluguelService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Post
    public HttpResponse<PedidoAluguelResponse> criar(@Body @Valid PedidoAluguelRequest request) {
        PedidoAluguelResponse response = pedidoService.criar(request);
        return HttpResponse.created(response)
                .headers(headers -> headers.location(URI.create("/pedidos/" + response.id())));
    }

    @Get("/{id}")
    public PedidoAluguelResponse buscarPorId(Long id) {
        return pedidoService.buscarPorId(id);
    }

    @Get("/cliente/{clienteId}")
    public List<PedidoAluguelResponse> listarPorCliente(Long clienteId) {
        return pedidoService.listarPorCliente(clienteId);
    }

    @Patch("/{id}/status/{status}")
    public PedidoAluguelResponse atualizarStatus(Long id, StatusPedido status) {
        return pedidoService.atualizarStatus(id, status);
    }
}