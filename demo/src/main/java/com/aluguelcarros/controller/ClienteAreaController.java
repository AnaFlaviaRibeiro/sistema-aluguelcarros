package com.aluguelcarros.controller;

import com.aluguelcarros.request.AtualizarClienteRequest;
import com.aluguelcarros.request.PedidoAtualizacaoClienteRequest;
import com.aluguelcarros.request.PedidoCriacaoClienteRequest;
import com.aluguelcarros.response.ClientePerfilResponse;
import com.aluguelcarros.response.PedidoAluguelResponse;
import com.aluguelcarros.security.AuthPrincipal;
import com.aluguelcarros.service.ClienteService;
import com.aluguelcarros.service.PedidoAluguelService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/cliente")
public class ClienteAreaController {

    private final ClienteService clienteService;
    private final PedidoAluguelService pedidoAluguelService;

    public ClienteAreaController(ClienteService clienteService, PedidoAluguelService pedidoAluguelService) {
        this.clienteService = clienteService;
        this.pedidoAluguelService = pedidoAluguelService;
    }

    private static Long idCliente(HttpRequest<?> request) {
        return request.getAttribute(AuthPrincipal.ATTRIBUTE, AuthPrincipal.class)
                .orElseThrow()
                .userId();
    }

    @Get("/perfil")
    public ClientePerfilResponse perfil(HttpRequest<?> request) {
        return clienteService.buscarPerfilPorUsuarioId(idCliente(request));
    }

    @Put("/perfil")
    public ClientePerfilResponse atualizarPerfil(
            HttpRequest<?> request,
            @Body @Valid AtualizarClienteRequest body) {
        return clienteService.atualizarPerfil(idCliente(request), body);
    }

    @Get("/pedidos")
    public List<PedidoAluguelResponse> listarPedidos(HttpRequest<?> request) {
        return pedidoAluguelService.listarPorCliente(idCliente(request));
    }

    @Get("/pedidos/{id}")
    public PedidoAluguelResponse buscarPedido(HttpRequest<?> request, Long id) {
        return pedidoAluguelService.buscarPorIdParaCliente(idCliente(request), id);
    }

    @Post("/pedidos")
    public PedidoAluguelResponse criarPedido(
            HttpRequest<?> request,
            @Body @Valid PedidoCriacaoClienteRequest body) {
        return pedidoAluguelService.criarParaCliente(idCliente(request), body);
    }

    @Put("/pedidos/{id}")
    public PedidoAluguelResponse atualizarPedido(
            HttpRequest<?> request,
            Long id,
            @Body @Valid PedidoAtualizacaoClienteRequest body) {
        return pedidoAluguelService.atualizarPedidoCliente(idCliente(request), id, body);
    }

    @Patch("/pedidos/{id}/cancelar")
    public PedidoAluguelResponse cancelarPedido(HttpRequest<?> request, Long id) {
        return pedidoAluguelService.cancelarPedidoCliente(idCliente(request), id);
    }
}
