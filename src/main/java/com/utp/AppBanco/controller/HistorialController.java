package com.utp.AppBanco.controller;

import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Recarga;
import com.utp.AppBanco.model.Retiro;
import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.service.RecargaRetiroService;
import com.utp.AppBanco.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * STATE: esta pantalla es la vitrina de la gestión de estados de transacciones.
 * Muestra, para el usuario logueado, todas sus recargas y retiros junto con
 * el estado actual de cada operación (SOLICITADO / VALIDADO / ENTREGADO),
 * usando las propiedades visuales (color e ícono) que ya trae el enum EstadoOperacion.
 */
@Controller
public class HistorialController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RecargaRetiroService recargaRetiroService;

    @GetMapping("/historial")
    public String verHistorial(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Cuenta> cuentas = usuarioService.obtenerCuentasPorUsuario(usuario);

        // Un usuario puede tener varias cuentas (multibanco), así que juntamos
        // las recargas y retiros de TODAS sus cuentas en una sola vista.
        List<Recarga> recargas = new ArrayList<>();
        List<Retiro> retiros = new ArrayList<>();

        for (Cuenta cuenta : cuentas) {
            recargas.addAll(recargaRetiroService.obtenerRecargasPorCuenta(cuenta.getNumeroCuenta()));
            retiros.addAll(recargaRetiroService.obtenerRetirosPorCuenta(cuenta.getNumeroCuenta()));
        }

        model.addAttribute("recargas", recargas);
        model.addAttribute("retiros", retiros);
        return "historial";
    }
}
