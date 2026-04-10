package com.aluguelcarros.service;

import com.aluguelcarros.exception.RecursoException;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.request.ClienteRequest;
import com.aluguelcarros.response.ClienteResponse;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponse salvar(ClienteRequest request) {
        validarDuplicidade(request, null);

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setSenha(request.getSenha());
        cliente.setRg(request.getRg());
        cliente.setCpf(request.getCpf());
        cliente.setEndereco(request.getEndereco());
        cliente.setProfissao(request.getProfissao());

        Cliente salvo = clienteRepository.save(cliente);
        return toResponse(salvo);
    }

    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado com id: " + id));
        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado com id: " + id));

        validarDuplicidade(request, id);

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setSenha(request.getSenha());
        cliente.setRg(request.getRg());
        cliente.setCpf(request.getCpf());
        cliente.setEndereco(request.getEndereco());
        cliente.setProfissao(request.getProfissao());

        Cliente atualizado = clienteRepository.update(cliente);
        return toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado com id: " + id));
        clienteRepository.delete(cliente);
    }

    private void validarDuplicidade(ClienteRequest request, Long idAtual) {
        clienteRepository.findByEmail(request.getEmail()).ifPresent(cliente -> {
            if (idAtual == null || !cliente.getId().equals(idAtual)) {
                throw new IllegalArgumentException("J\u00e1 existe cliente com este e-mail.");
            }
        });

        clienteRepository.findByCpf(request.getCpf()).ifPresent(cliente -> {
            if (idAtual == null || !cliente.getId().equals(idAtual)) {
                throw new IllegalArgumentException("J\u00e1 existe cliente com este CPF.");
            }
        });
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getRg(),
                cliente.getCpf(),
                cliente.getEndereco(),
                cliente.getProfissao()
        );
    }
}
