package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Retiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetiroRepository extends JpaRepository<Retiro, Long> {
    List<Retiro> findByCuenta_NumeroCuentaOrderByFechaDesc(String numeroCuenta);
}
