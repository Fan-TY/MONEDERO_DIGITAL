package com.utp.AppBanco.pattern.factory;

import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.pattern.prototype.PerfilConfiguracion;
import com.utp.AppBanco.pattern.state.EstadoUsuario;

public class UsuarioFactory {

    public enum TipoUsuario {
        CLIENTE, COMERCIO, ADMIN
    }

    private static final PerfilConfiguracion CONFIG_BASE = PerfilConfiguracion.crearConfiguracionBase();

    public static Usuario crear(TipoUsuario tipo, String nombre, String dni, String password) {
        return switch (tipo) {
            case CLIENTE  -> crearUsuarioCliente(nombre, dni, password);
            case COMERCIO -> crearUsuarioComercio(nombre, dni, password);
            case ADMIN    -> crearUsuarioAdmin(nombre, dni, password);
        };
    }

    private static Usuario crearUsuarioCliente(String nombre, String dni, String password) {
        System.out.println("[FACTORY] Creando UsuarioCliente → DNI: " + dni);

        PerfilConfiguracion config = CONFIG_BASE.clonarPara(nombre);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDni(dni);
        usuario.setPassword(password);
        usuario.setTipoUsuario("CLIENTE");
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setPerfilConfiguracion(config);

        System.out.println("[FACTORY] UsuarioCliente creado con estado: " + EstadoUsuario.ACTIVO.getEtiqueta());
        return usuario;
    }

    private static Usuario crearUsuarioComercio(String nombre, String dni, String password) {
        System.out.println("[FACTORY] Creando UsuarioComercio → DNI: " + dni);

        PerfilConfiguracion config = CONFIG_BASE.clonarPara(nombre);
        config.setLimiteDiarioTransferencia(10000);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDni(dni);
        usuario.setPassword(password);
        usuario.setTipoUsuario("COMERCIO");
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setPerfilConfiguracion(config);
        return usuario;
    }

    private static Usuario crearUsuarioAdmin(String nombre, String dni, String password) {
        System.out.println("[FACTORY] Creando UsuarioAdmin → DNI: " + dni);

        PerfilConfiguracion config = CONFIG_BASE.clonarPara(nombre);
        config.setLimiteDiarioTransferencia(Integer.MAX_VALUE);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setDni(dni);
        usuario.setPassword(password);
        usuario.setTipoUsuario("ADMIN");
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setPerfilConfiguracion(config);
        return usuario;
    }
}