package com.aluguelcarros.repository;

import com.aluguelcarros.model.Contrato;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
}
