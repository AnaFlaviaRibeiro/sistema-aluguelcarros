package com.aluguelcarros.repository;

import com.aluguelcarros.model.PedidoAluguel;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    List<PedidoAluguel> findByClienteId(Long clienteId);

    @Query("SELECT p FROM PedidoAluguel p ORDER BY p.dataPedido DESC")
    List<PedidoAluguel> listarTodosPorDataDesc();
}
