package com.aluguelcarros.controller;

import com.aluguelcarros.model.type.StatusPedido;
import com.aluguelcarros.request.ContratoVinculoRequest;
import com.aluguelcarros.request.CreditoVinculoRequest;
import com.aluguelcarros.response.ClientePerfilResponse;
import com.aluguelcarros.response.PedidoAluguelResponse;
import com.aluguelcarros.security.AuthPrincipal;
import com.aluguelcarros.service.ContratoRegistroService;
import com.aluguelcarros.service.PedidoAluguelService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/agente")
public class AgenteAreaController {

    private final PedidoAluguelService pedidoAluguelService;
    private final ContratoRegistroService contratoRegistroService;

    public AgenteAreaController(
            PedidoAluguelService pedidoAluguelService,
            ContratoRegistroService contratoRegistroService) {
        this.pedidoAluguelService = pedidoAluguelService;
        this.contratoRegistroService = contratoRegistroService;
    }

    private static Long idAgente(HttpRequest<?> request) {
        return request.getAttribute(AuthPrincipal.ATTRIBUTE, AuthPrincipal.class)
                .orElseThrow()
                .userId();
    }

    @Get("/pedidos")
    public List<PedidoAluguelResponse> listarPedidos() {
        return pedidoAluguelService.listarTodosParaAgente();
    }

    @Get("/pedidos/{id}/contratante")
    public ClientePerfilResponse dadosContratanteDoPedido(Long id) {
        return pedidoAluguelService.dadosContratanteDoPedido(id);
    }

    @Patch("/pedidos/{id}/status/{status}")
    public PedidoAluguelResponse avaliar(HttpRequest<?> request, Long id, StatusPedido status) {
        return pedidoAluguelService.avaliarPorAgente(idAgente(request), id, status);
    }

    @Post("/pedidos/{id}/contrato")
    public PedidoAluguelResponse registrarContrato(
            HttpRequest<?> request,
            Long id,
            @Body @Valid ContratoVinculoRequest body) {
        return contratoRegistroService.registrarContrato(idAgente(request), id, body);
    }

    @Post("/pedidos/{id}/credito")
    public PedidoAluguelResponse vincularCredito(
            HttpRequest<?> request,
            Long id,
            @Body @Valid CreditoVinculoRequest body) {
        return contratoRegistroService.vincularCreditoBancario(idAgente(request), id, body);
    }
}
