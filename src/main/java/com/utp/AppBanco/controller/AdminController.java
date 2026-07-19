package com.utp.AppBanco.controller;

import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.pattern.state.EstadoUsuario;
import com.utp.AppBanco.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * STATE: panel administrativo para gestionar el estado de los usuarios
 * (ACTIVO / SUSPENDIDO / BLOQUEADO). Solo un usuario con tipoUsuario = ADMIN
 * puede acceder; cualquier otro es redirigido a su perfil.
 */
@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    private boolean esAdmin(Usuario usuario) {
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getTipoUsuario());
    }

    @GetMapping("/admin/usuarios")
    public String verPanelUsuarios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        if (!esAdmin(usuario)) return "redirect:/perfil";

        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        model.addAttribute("estados", EstadoUsuario.values());
        return "admin-usuarios";
    }

    @PostMapping("/admin/usuarios/cambiar-estado")
    public String cambiarEstado(
            @RequestParam("dni") String dni,
            @RequestParam("nuevoEstado") EstadoUsuario nuevoEstado,
            HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";
        if (!esAdmin(usuario)) return "redirect:/perfil";

        try {
            // STATE: aquí ocurre la transición real de estado del usuario
            usuarioService.cambiarEstadoUsuario(dni, nuevoEstado);
            model.addAttribute("mensajeExito", "Estado actualizado correctamente para el DNI " + dni + ".");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        model.addAttribute("estados", EstadoUsuario.values());
        return "admin-usuarios";
    }
}
