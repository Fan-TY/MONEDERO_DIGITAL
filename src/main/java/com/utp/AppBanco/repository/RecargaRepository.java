package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Recarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecargaRepository extends JpaRepository<Recarga, Long> {
    List<Recarga> findByCuenta_NumeroCuentaOrderByFechaDesc(String numeroCuenta);
}
