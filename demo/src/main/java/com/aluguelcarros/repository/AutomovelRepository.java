package com.aluguelcarros.repository;

import com.aluguelcarros.model.Automovel;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
}
