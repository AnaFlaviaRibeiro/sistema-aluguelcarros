package com.aluguelcarros.service;

import com.aluguelcarros.exception.RecursoException;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Contrato;
import com.aluguelcarros.model.ContratoCredito;
import com.aluguelcarros.model.PedidoAluguel;
import com.aluguelcarros.model.type.StatusCredito;
import com.aluguelcarros.model.type.StatusPedido;
import com.aluguelcarros.model.type.TipoAgente;
import com.aluguelcarros.model.type.TipoContrato;
import com.aluguelcarros.repository.AgenteRepository;
import com.aluguelcarros.repository.ContratoCreditoRepository;
import com.aluguelcarros.repository.ContratoRepository;
import com.aluguelcarros.repository.PedidoAluguelRepository;
import com.aluguelcarros.request.ContratoVinculoRequest;
import com.aluguelcarros.response.PedidoAluguelResponse;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Singleton
public class ContratoRegistroService {

    private final PedidoAluguelRepository pedidoRepository;
    private final AgenteRepository agenteRepository;
    private final ContratoRepository contratoRepository;
    private final ContratoCreditoRepository contratoCreditoRepository;
    private final PedidoAluguelService pedidoAluguelService;

    public ContratoRegistroService(
            PedidoAluguelRepository pedidoRepository,
            AgenteRepository agenteRepository,
            ContratoRepository contratoRepository,
            ContratoCreditoRepository contratoCreditoRepository,
            PedidoAluguelService pedidoAluguelService) {
        this.pedidoRepository = pedidoRepository;
        this.agenteRepository = agenteRepository;
        this.contratoRepository = contratoRepository;
        this.contratoCreditoRepository = contratoCreditoRepository;
        this.pedidoAluguelService = pedidoAluguelService;
    }

    @Transactional
    public PedidoAluguelResponse registrarContrato(
            Long agenteUsuarioId,
            Long pedidoId,
            ContratoVinculoRequest request) {
        Agente agente = agenteRepository.findById(agenteUsuarioId)
                .orElseThrow(() -> new RecursoException("Agente n\u00e3o encontrado."));
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado."));

        if (pedido.getStatus() != StatusPedido.APROVADO) {
            throw new IllegalArgumentException(
                    "O contrato s\u00f3 pode ser registrado ap\u00f3s parecer financeiro positivo (APROVADO).");
        }
        if (pedido.getContrato() != null) {
            throw new IllegalArgumentException("Este pedido j\u00e1 possui contrato registrado.");
        }

        if (request.getTipoContrato() == TipoContrato.COM_CREDITO) {
            if (agente.getTipoAgente() != TipoAgente.BANCO) {
                throw new IllegalArgumentException(
                        "Apenas agentes do tipo BANCO podem vincular contrato de cr\u00e9dito.");
            }
            if (request.getNumeroCredito() == null || request.getNumeroCredito().isBlank()) {
                throw new IllegalArgumentException("N\u00famero do cr\u00e9dito \u00e9 obrigat\u00f3rio para contrato com cr\u00e9dito.");
            }
            if (request.getValorAprovado() == null
                    || request.getValorAprovado().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor aprovado do cr\u00e9dito \u00e9 obrigat\u00f3rio.");
            }
        } else if (request.getNumeroCredito() != null || request.getValorAprovado() != null) {
            throw new IllegalArgumentException(
                    "Cr\u00e9dito s\u00f3 deve ser informado quando o tipo de contrato \u00e9 COM_CREDITO.");
        }

        Contrato contrato = new Contrato();
        contrato.setNumeroContrato(request.getNumeroContrato());
        contrato.setDataInicio(request.getDataInicio());
        contrato.setDataFim(request.getDataFim());
        contrato.setTipoContrato(request.getTipoContrato());
        contrato.setPedidoAluguel(pedido);
        pedido.setContrato(contrato);

        contratoRepository.save(contrato);

        if (request.getTipoContrato() == TipoContrato.COM_CREDITO) {
            ContratoCredito credito = new ContratoCredito();
            credito.setContrato(contrato);
            credito.setBancoAgente(agente);
            credito.setNumeroCredito(request.getNumeroCredito());
            credito.setValorAprovado(request.getValorAprovado());
            credito.setStatusCredito(StatusCredito.APROVADO);
            contratoCreditoRepository.save(credito);
            contrato.setContratoCredito(credito);
        }

        pedidoRepository.update(pedido);
        return pedidoAluguelService.buscarPorId(pedidoId);
    }
}
