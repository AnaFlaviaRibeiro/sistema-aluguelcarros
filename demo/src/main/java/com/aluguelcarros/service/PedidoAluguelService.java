package com.aluguelcarros.service;

import com.aluguelcarros.exception.RecursoException;
import com.aluguelcarros.model.Automovel;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.model.PedidoAluguel;
import com.aluguelcarros.model.type.StatusPedido;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.repository.PedidoAluguelRepository;
import com.aluguelcarros.request.PedidoAluguelRequest;
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

    public PedidoAluguelService(PedidoAluguelRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public PedidoAluguelResponse criar(PedidoAluguelRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoException("Cliente não encontrado com id: " + request.getClienteId()));

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

    public PedidoAluguelResponse buscarPorId(Long id) {
        PedidoAluguel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Pedido não encontrado com id: " + id));
        return toResponse(pedido);
    }

    public List<PedidoAluguelResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoAluguelResponse atualizarStatus(Long id, StatusPedido status) {
        PedidoAluguel pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Pedido não encontrado com id: " + id));

        pedido.setStatus(status);
        PedidoAluguel atualizado = pedidoRepository.update(pedido);
        return toResponse(atualizado);
    }

    private PedidoAluguelResponse toResponse(PedidoAluguel pedido) {
        return new PedidoAluguelResponse(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getStatus(),
                pedido.getValorMensal(),
                pedido.getPrazoMeses(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getAutomovel().getPlaca(),
                pedido.getAutomovel().getModelo()
        );
    }
}
