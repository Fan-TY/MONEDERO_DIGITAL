package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    boolean existsByDni(String dni);
    Usuario findByDni(String dni);

    @Procedure(procedureName = "sp_registrar_usuario_con_cuenta")
    void registrarUsuarioConCuentaSP(
        @Param("p_nombre") String nombre,
        @Param("p_dni") String dni,
        @Param("p_password") String password,
        @Param("p_numero_cuenta") String numeroCuenta,
        @Param("p_banco") String banco,
        @Param("p_saldo") double saldo
    );
}