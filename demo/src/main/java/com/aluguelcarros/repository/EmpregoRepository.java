package com.aluguelcarros.repository;

import com.aluguelcarros.model.Emprego;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface EmpregoRepository extends JpaRepository<Emprego, Long> {
}
