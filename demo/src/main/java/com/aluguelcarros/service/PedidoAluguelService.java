package com.aluguelcarros.service;

import com.aluguelcarros.exception.AcessoNegadoException;
import com.aluguelcarros.exception.RecursoException;
import com.aluguelcarros.model.Agente;
import com.aluguelcarros.model.Automovel;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.model.Contrato;
import com.aluguelcarros.model.ContratoCredito;
import com.aluguelcarros.model.PedidoAluguel;
import com.aluguelcarros.model.type.StatusPedido;
import com.aluguelcarros.repository.AgenteRepository;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.repository.PedidoAluguelRepository;
import com.aluguelcarros.request.PedidoAtualizacaoClienteRequest;
import com.aluguelcarros.request.PedidoCriacaoClienteRequest;
import com.aluguelcarros.response.ClientePerfilResponse;
import com.aluguelcarros.response.PedidoAluguelResponse;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PedidoAluguelService {

    private final PedidoAluguelRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final ClienteService clienteService;

    public PedidoAluguelService(
            PedidoAluguelRepository pedidoRepository,
            ClienteRepository clienteRepository,
            AgenteRepository agenteRepository,
            ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
        this.clienteService = clienteService;
    }

    @Transactional
    public ClientePerfilResponse dadosContratanteDoPedido(Long pedidoId) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + pedidoId));
        return clienteService.buscarPerfilContratanteParaAnalise(pedido.getCliente().getId());
    }

    @Transactional
    public PedidoAluguelResponse criarParaCliente(Long clienteUsuarioId, PedidoCriacaoClienteRequest request) {
        Cliente cliente = clienteRepository.findById(clienteUsuarioId)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado."));

        Automovel automovel = new Automovel();
        automovel.setMatricula(request.getMatricula());
        automovel.setAno(request.getAno());
        automovel.setMarca(request.getMarca());
        automovel.setModelo(request.getModelo());
        automovel.setPlaca(request.getPlaca());
        automovel.setProprietarioTipo(request.getProprietarioTipo());

        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorMensal(request.getValorMensal());
        pedido.setPrazoMeses(request.getPrazoMeses());

        PedidoAluguel salvo = pedidoRepository.save(pedido);
        return toResponse(salvo);
    }

    @Transactional
    public PedidoAluguelResponse buscarPorId(Long id) {
        PedidoAluguel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + id));
        return toResponse(pedido);
    }

    @Transactional
    public PedidoAluguelResponse buscarPorIdParaCliente(Long clienteUsuarioId, Long pedidoId) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + pedidoId));
        garantirDono(clienteUsuarioId, pedido);
        return toResponse(pedido);
    }

    @Transactional
    public List<PedidoAluguelResponse> listarPorCliente(Long clienteUsuarioId) {
        return pedidoRepository.findByClienteId(clienteUsuarioId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PedidoAluguelResponse> listarTodosParaAgente() {
        return pedidoRepository.listarTodosPorDataDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoAluguelResponse atualizarPedidoCliente(
            Long clienteUsuarioId,
            Long pedidoId,
            PedidoAtualizacaoClienteRequest request) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + pedidoId));
        garantirDono(clienteUsuarioId, pedido);
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new IllegalArgumentException(
                    "Apenas pedidos com status PENDENTE podem ser alterados pelo cliente.");
        }

        pedido.setValorMensal(request.getValorMensal());
        pedido.setPrazoMeses(request.getPrazoMeses());
        Automovel a = pedido.getAutomovel();
        a.setMatricula(request.getMatricula());
        a.setAno(request.getAno());
        a.setMarca(request.getMarca());
        a.setModelo(request.getModelo());
        a.setPlaca(request.getPlaca());
        a.setProprietarioTipo(request.getProprietarioTipo());

        PedidoAluguel atualizado = pedidoRepository.update(pedido);
        return toResponse(atualizado);
    }

    @Transactional
    public PedidoAluguelResponse cancelarPedidoCliente(Long clienteUsuarioId, Long pedidoId) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + pedidoId));
        garantirDono(clienteUsuarioId, pedido);
        if (pedido.getStatus() != StatusPedido.PENDENTE && pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new IllegalArgumentException(
                    "S\u00f3 \u00e9 poss\u00edvel cancelar pedidos pendentes ou em an\u00e1lise.");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return toResponse(pedidoRepository.update(pedido));
    }

    @Transactional
    public PedidoAluguelResponse avaliarPorAgente(Long agenteUsuarioId, Long pedidoId, StatusPedido novoStatus) {
        Agente agente = agenteRepository.findById(agenteUsuarioId)
                .orElseThrow(() -> new RecursoException("Agente n\u00e3o encontrado."));
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoException("Pedido n\u00e3o encontrado com id: " + pedidoId));

        validarTransicaoAgente(pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);
        pedido.setAgenteAvaliador(agente);
        return toResponse(pedidoRepository.update(pedido));
    }

    private static void validarTransicaoAgente(StatusPedido atual, StatusPedido novo) {
        if (novo == StatusPedido.CANCELADO || novo == StatusPedido.PENDENTE) {
            throw new IllegalArgumentException("Este status n\u00e3o pode ser definido pelo agente.");
        }
        switch (atual) {
            case PENDENTE:
                if (novo != StatusPedido.EM_ANALISE && novo != StatusPedido.REPROVADO) {
                    throw new IllegalArgumentException(
                            "A partir de PENDENTE, o agente deve colocar o pedido em an\u00e1lise ou reprov\u00e1-lo.");
                }
                break;
            case EM_ANALISE:
                if (novo != StatusPedido.APROVADO && novo != StatusPedido.REPROVADO) {
                    throw new IllegalArgumentException(
                            "A partir de EM_ANALISE, o agente deve aprovar ou reprovar financeiramente.");
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "N\u00e3o \u00e9 poss\u00edvel alterar o status deste pedido nesta fase.");
        }
    }

    private static void garantirDono(Long clienteUsuarioId, PedidoAluguel pedido) {
        if (!pedido.getCliente().getId().equals(clienteUsuarioId)) {
            throw new AcessoNegadoException("Este pedido n\u00e3o pertence ao cliente autenticado.");
        }
    }

    public PedidoAluguelResponse toResponse(PedidoAluguel pedido) {
        var a = pedido.getAutomovel();
        var ag = pedido.getAgenteAvaliador();
        Contrato c = pedido.getContrato();
        String numeroContrato = null;
        String tipoContrato = null;
        String numeroCredito = null;
        String statusCredito = null;
        if (c != null) {
            numeroContrato = c.getNumeroContrato();
            tipoContrato = c.getTipoContrato().name();
            ContratoCredito cc = c.getContratoCredito();
            if (cc != null) {
                numeroCredito = cc.getNumeroCredito();
                statusCredito = cc.getStatusCredito().name();
            }
        }
        return new PedidoAluguelResponse(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getStatus(),
                pedido.getValorMensal(),
                pedido.getPrazoMeses(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                a.getMatricula(),
                a.getAno(),
                a.getMarca(),
                a.getPlaca(),
                a.getModelo(),
                a.getProprietarioTipo().name(),
                ag != null ? ag.getNome() : null,
                numeroContrato,
                tipoContrato,
                numeroCredito,
                statusCredito
        );
    }
}
