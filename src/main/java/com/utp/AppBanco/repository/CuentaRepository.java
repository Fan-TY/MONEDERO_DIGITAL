package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    List<Cuenta> findByUsuario(Usuario usuario);
}