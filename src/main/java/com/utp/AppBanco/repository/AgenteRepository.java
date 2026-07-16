package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, String> {
    List<Agente> findByActivoTrue();
}
