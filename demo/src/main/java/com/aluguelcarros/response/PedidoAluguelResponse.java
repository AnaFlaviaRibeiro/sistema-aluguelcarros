package com.aluguelcarros.response;

import com.aluguelcarros.model.type.StatusPedido;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Serdeable
@Introspected
public record PedidoAluguelResponse(
        Long id,
        LocalDate dataPedido,
        StatusPedido status,
        BigDecimal valorMensal,
        Integer prazoMeses,
        Long clienteId,
        String nomeCliente,
        String placaAutomovel,
        String modeloAutomovel
) {
}
