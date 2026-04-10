package com.aluguelcarros.service;

import com.aluguelcarros.exception.RecursoException;
import com.aluguelcarros.model.Cliente;
import com.aluguelcarros.model.Emprego;
import com.aluguelcarros.repository.ClienteRepository;
import com.aluguelcarros.request.AtualizarClienteRequest;
import com.aluguelcarros.request.EmpregoRequest;
import com.aluguelcarros.request.RegistroClienteRequest;
import com.aluguelcarros.response.ClientePerfilResponse;
import com.aluguelcarros.response.EmpregoResponse;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClientePerfilResponse registrar(RegistroClienteRequest request) {
        validarDuplicidadeRegistro(request);

        Cliente cliente = new Cliente();
        preencherDados(cliente, request.getNome(), request.getEmail(),
                passwordEncoder.encode(request.getSenha()),
                request.getRg(), request.getCpf(), request.getEndereco(), request.getProfissao());
        aplicarEmpregos(cliente, request.getEmpregos());

        Cliente salvo = clienteRepository.save(cliente);
        return toPerfil(salvo);
    }

    @Transactional
    public ClientePerfilResponse buscarPerfilPorUsuarioId(Long usuarioId) {
        Cliente cliente = clienteRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado."));
        cliente.getEmpregos().size();
        return toPerfil(cliente);
    }

    /**
     * Mesmo conte\u00fado do perfil (identifica\u00e7\u00e3o + empregos/rendas) para an\u00e1lise por agente.
     * O id do cliente \u00e9 o mesmo do usu\u00e1rio na hierarquia atual.
     */
    @Transactional
    public ClientePerfilResponse buscarPerfilContratanteParaAnalise(Long clienteId) {
        return buscarPerfilPorUsuarioId(clienteId);
    }

    @Transactional
    public ClientePerfilResponse atualizarPerfil(Long usuarioId, AtualizarClienteRequest request) {
        Cliente cliente = clienteRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado."));

        validarDuplicidadeAtualizacao(request, usuarioId);

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setRg(request.getRg());
        cliente.setCpf(request.getCpf());
        cliente.setEndereco(request.getEndereco());
        cliente.setProfissao(request.getProfissao());
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        cliente.getEmpregos().clear();
        aplicarEmpregos(cliente, request.getEmpregos());

        Cliente atualizado = clienteRepository.update(cliente);
        return toPerfil(atualizado);
    }

    public Cliente obterClientePorUsuarioId(Long usuarioId) {
        return clienteRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoException("Cliente n\u00e3o encontrado."));
    }

    private void validarDuplicidadeRegistro(RegistroClienteRequest request) {
        clienteRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            throw new IllegalArgumentException("J\u00e1 existe cadastro com este e-mail.");
        });
        clienteRepository.findByCpf(request.getCpf()).ifPresent(c -> {
            throw new IllegalArgumentException("J\u00e1 existe cadastro com este CPF.");
        });
    }

    private void validarDuplicidadeAtualizacao(AtualizarClienteRequest request, Long idAtual) {
        clienteRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            if (!c.getId().equals(idAtual)) {
                throw new IllegalArgumentException("J\u00e1 existe cadastro com este e-mail.");
            }
        });
        clienteRepository.findByCpf(request.getCpf()).ifPresent(c -> {
            if (!c.getId().equals(idAtual)) {
                throw new IllegalArgumentException("J\u00e1 existe cadastro com este CPF.");
            }
        });
    }

    private static void preencherDados(
            Cliente cliente,
            String nome,
            String email,
            String senhaHash,
            String rg,
            String cpf,
            String endereco,
            String profissao) {
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(senhaHash);
        cliente.setRg(rg);
        cliente.setCpf(cpf);
        cliente.setEndereco(endereco);
        cliente.setProfissao(profissao);
    }

    private static void aplicarEmpregos(Cliente cliente, List<EmpregoRequest> empregos) {
        if (empregos == null) {
            return;
        }
        for (EmpregoRequest er : empregos) {
            Emprego e = new Emprego();
            e.setEntidadeEmpregadora(er.getEntidadeEmpregadora());
            e.setRenda(er.getRenda());
            e.setCliente(cliente);
            cliente.getEmpregos().add(e);
        }
    }

    private ClientePerfilResponse toPerfil(Cliente cliente) {
        List<EmpregoResponse> emp = cliente.getEmpregos().stream()
                .map(e -> new EmpregoResponse(e.getId(), e.getEntidadeEmpregadora(), e.getRenda()))
                .collect(Collectors.toList());
        return new ClientePerfilResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getRg(),
                cliente.getCpf(),
                cliente.getEndereco(),
                cliente.getProfissao(),
                emp
        );
    }
}
