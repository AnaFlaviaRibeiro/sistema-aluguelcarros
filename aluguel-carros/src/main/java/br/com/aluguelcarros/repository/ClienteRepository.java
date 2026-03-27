package br.com.aluguelcarros.repository;

import br.com.aluguelcarros.model.Cliente;
import br.com.aluguelcarros.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class ClienteRepository {

    public void salvar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (cliente.getId() == null) {
                em.persist(cliente);
            } else {
                em.merge(cliente);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Cliente buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public List<Cliente> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void remover(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}