package com.aluguelcarros.repository;

import com.aluguelcarros.model.ContratoCredito;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface ContratoCreditoRepository extends JpaRepository<ContratoCredito, Long> {
}
