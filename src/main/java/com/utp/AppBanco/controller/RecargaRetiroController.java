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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RecargaRetiroController {

    @Autowired
    private RecargaRetiroService recargaRetiroService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/recargar")
    public String mostrarRecarga(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        List<Cuenta> cuentas = usuarioService.obtenerCuentasPorUsuario(usuario);
        model.addAttribute("cuentas", cuentas);
        model.addAttribute("todasLasCuentas", recargaRetiroService.obtenerTodasLasCuentas());
        return "recargar";
    }

    @PostMapping("/recargar")
    public String procesarRecarga(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("monto") double monto,
            @RequestParam("tipoOrigen") String tipoOrigen,
            @RequestParam("referencia") String referencia,
            HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        try {
            Recarga recarga = recargaRetiroService.recargarSaldo(numeroCuenta, monto, tipoOrigen, referencia);
            String mensaje = "Recarga procesada correctamente. Comprobante: " + recarga.getComprobante();
            if (recarga.getTitularOrigen() != null) {
                mensaje += " | Transferencia interna desde la cuenta de: " + recarga.getTitularOrigen();
            }
            model.addAttribute("mensajeExito", mensaje);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("cuentas", usuarioService.obtenerCuentasPorUsuario(usuario));
        model.addAttribute("todasLasCuentas", recargaRetiroService.obtenerTodasLasCuentas());
        return "recargar";
    }


    @GetMapping("/retirar")
    public String mostrarRetiro(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("cuentas", usuarioService.obtenerCuentasPorUsuario(usuario));
        model.addAttribute("agentes", recargaRetiroService.obtenerAgentesDisponibles());
        return "retirar";
    }

    @PostMapping("/retirar")
    public String procesarRetiro(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("codigoAgente") String codigoAgente,
            @RequestParam("monto") double monto,
            @RequestParam(value = "biometriaValida", defaultValue = "false") boolean biometriaValida,
            HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        try {
            Retiro retiro = recargaRetiroService.retirarEnAgente(numeroCuenta, codigoAgente, monto, biometriaValida);
            model.addAttribute("mensajeExito", "Retiro entregado. Comprobante: " + retiro.getComprobante());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("cuentas", usuarioService.obtenerCuentasPorUsuario(usuario));
        model.addAttribute("agentes", recargaRetiroService.obtenerAgentesDisponibles());
        return "retirar";
    }
}
