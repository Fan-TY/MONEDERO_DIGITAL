package com.utp.AppBanco.service;

import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.pattern.factory.UsuarioFactory;
import com.utp.AppBanco.pattern.singleton.DatabaseConnectionManager;
import com.utp.AppBanco.pattern.state.EstadoUsuario;
import com.utp.AppBanco.repository.CuentaRepository;
import com.utp.AppBanco.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private DatabaseConnectionManager dbManager; 

    @Transactional
    public void registrarUsuarioConCuenta(Usuario datosFormulario, String numeroCuenta, String banco, double saldoInicial) {

        // Validaciones previas
        if (usuarioRepository.existsByDni(datosFormulario.getDni())) {
            throw new IllegalArgumentException("El DNI ya se encuentra registrado.");
        }
        if (cuentaRepository.existsById(numeroCuenta)) {
            throw new IllegalArgumentException("El número de cuenta ya está registrado.");
        }
        System.out.println("[SINGLETON] Registro iniciado. Estado BD: " + dbManager.getEstadoConexion());

        Usuario usuarioPreparado = UsuarioFactory.crear(
                UsuarioFactory.TipoUsuario.CLIENTE,
                datosFormulario.getNombre(),
                datosFormulario.getDni(),
                datosFormulario.getPassword()
        );

        usuarioRepository.registrarUsuarioConCuentaSP(
                usuarioPreparado.getNombre(),
                usuarioPreparado.getDni(),
                usuarioPreparado.getPassword(),
                numeroCuenta,
                banco,
                saldoInicial
        );

        System.out.println("[REGISTRO COMPLETO] SP ejecutado para DNI: " + usuarioPreparado.getDni()
                + " | Tipo: " + usuarioPreparado.getTipoUsuario()
                + " | Estado: " + usuarioPreparado.getEstado().getEtiqueta());
    }
    public Usuario validarLogin(String dni, String password) {
        Usuario usuario = usuarioRepository.findByDni(dni);

        if (usuario == null) return null;

        if (!usuario.puedeIniciarSesion()) {
            System.out.println("[STATE] Login rechazado para DNI " + dni
                    + ". Estado: " + usuario.getEstado().getEtiqueta());
            throw new IllegalStateException(usuario.getEstado().getMensaje());
        }

        if (!usuario.getPassword().equals(password)) return null;

        System.out.println("[STATE] Login exitoso. DNI: " + usuario.getDni()
                + " | Estado: " + usuario.getEstado().getEtiqueta());
        return usuario;
    }

    @Transactional
    public void cambiarEstadoUsuario(String dni, EstadoUsuario nuevoEstado) {
        Usuario usuario = usuarioRepository.findByDni(dni);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");

        usuario.cambiarEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }
    @Transactional
    public void agregarNuevaCuenta(String dni, Cuenta nuevaCuenta) {
        Usuario usuario = obtenerPerfilPorDni(dni);
        if (usuario == null) throw new IllegalArgumentException("Usuario no encontrado.");

        if (!usuario.puedeOperar()) {
            throw new IllegalStateException("No puedes vincular cuentas. Estado actual: "
                    + usuario.getEstado().getEtiqueta());
        }

        if (cuentaRepository.existsById(nuevaCuenta.getNumeroCuenta())) {
            throw new IllegalArgumentException("Esta cuenta ya está asociada al sistema.");
        }

        nuevaCuenta.setUsuario(usuario);
        cuentaRepository.save(nuevaCuenta);
    }


    public Usuario obtenerPerfilPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }

    public List<Cuenta> obtenerCuentasPorUsuario(Usuario usuario) {
        return cuentaRepository.findByUsuario(usuario);
    }
}