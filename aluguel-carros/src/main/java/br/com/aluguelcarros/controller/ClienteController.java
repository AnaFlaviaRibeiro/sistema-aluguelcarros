package br.com.aluguelcarros.controller;

import br.com.aluguelcarros.model.Cliente;
import br.com.aluguelcarros.model.Pessoa;
import br.com.aluguelcarros.model.Usuario;
import br.com.aluguelcarros.service.ClienteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/clientes")
public class ClienteController extends HttpServlet {

    private final ClienteService clienteService = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String acao = req.getParameter("acao");

        try {
            if (acao == null || acao.equals("listar")) {
                listar(req, resp);
            } else if (acao.equals("novo")) {
                abrirFormularioNovo(req, resp);
            } else if (acao.equals("editar")) {
                abrirFormularioEdicao(req, resp);
            } else if (acao.equals("excluir")) {
                excluir(req, resp);
            } else {
                listar(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/cliente/lista.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            salvar(req, resp);
        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("cliente", montarClienteFromRequest(req));
            req.getRequestDispatcher("/WEB-INF/views/cliente/form.jsp").forward(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cliente> clientes = clienteService.listarTodos();
        req.setAttribute("clientes", clientes);
        req.getRequestDispatcher("/WEB-INF/views/cliente/lista.jsp").forward(req, resp);
    }

    private void abrirFormularioNovo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/cliente/form.jsp").forward(req, resp);
    }

    private void abrirFormularioEdicao(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Cliente cliente = clienteService.buscarPorId(id);

        req.setAttribute("cliente", cliente);
        req.getRequestDispatcher("/WEB-INF/views/cliente/form.jsp").forward(req, resp);
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cliente cliente = montarClienteFromRequest(req);

        if (cliente.getId() == null) {
            clienteService.cadastrar(cliente);
        } else {
            clienteService.atualizar(cliente);
        }

        resp.sendRedirect(req.getContextPath() + "/clientes");
    }

    private void excluir(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        clienteService.excluir(id);
        resp.sendRedirect(req.getContextPath() + "/clientes");
    }

    private Cliente montarClienteFromRequest(HttpServletRequest req) {
        String idParam = req.getParameter("id");

        Cliente cliente = new Cliente();
        if (idParam != null && !idParam.trim().isEmpty()) {
            cliente.setId(Long.valueOf(idParam));
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(req.getParameter("login"));
        usuario.setSenha(req.getParameter("senha"));
        usuario.setAtivo(true);

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(req.getParameter("nome"));
        pessoa.setCpf(req.getParameter("cpf"));
        pessoa.setRg(req.getParameter("rg"));
        pessoa.setEndereco(req.getParameter("endereco"));
        pessoa.setProfissao(req.getParameter("profissao"));
        pessoa.setUsuario(usuario);

        cliente.setPessoa(pessoa);

        return cliente;
    }
}
