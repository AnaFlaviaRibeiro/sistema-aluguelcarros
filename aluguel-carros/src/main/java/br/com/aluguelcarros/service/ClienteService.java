package br.com.aluguelcarros.service;


import br.com.aluguelcarros.model.Cliente;
import br.com.aluguelcarros.model.Pessoa;
import br.com.aluguelcarros.model.Usuario;
import br.com.aluguelcarros.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public void cadastrar(Cliente cliente) {
        validarCliente(cliente);
        clienteRepository.salvar(cliente);
    }

    public void atualizar(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização.");
        }

        validarCliente(cliente);
        clienteRepository.salvar(cliente);
    }

    public Cliente buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo.");
        }

        Cliente cliente = clienteRepository.buscarPorId(id);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        return cliente;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public void excluir(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo.");
        }

        clienteRepository.remover(id);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório.");
        }

        Pessoa pessoa = cliente.getPessoa();
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa é obrigatória.");
        }

        if (isBlank(pessoa.getNome())) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }

        if (isBlank(pessoa.getCpf())) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }

        if (isBlank(pessoa.getRg())) {
            throw new IllegalArgumentException("RG é obrigatório.");
        }

        if (isBlank(pessoa.getEndereco())) {
            throw new IllegalArgumentException("Endereço é obrigatório.");
        }

        Usuario usuario = pessoa.getUsuario();
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário é obrigatório.");
        }

        if (isBlank(usuario.getLogin())) {
            throw new IllegalArgumentException("Login é obrigatório.");
        }

        if (isBlank(usuario.getSenha())) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
    }

    private boolean isBlank(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
