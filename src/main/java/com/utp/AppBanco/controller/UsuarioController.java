package com.utp.AppBanco.controller;

import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.service.NotificacionService;
import com.utp.AppBanco.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacionService notificacionService;

    // 1. Página de Bienvenida
    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    // 2. Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    // 3. Procesar registro
    // FACTORY + PROTOTYPE + SINGLETON se ejecutan dentro del service
    @PostMapping("/registro")
    public String registrarUsuario(
            @ModelAttribute("usuario") Usuario usuario,
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("banco") String banco,
            @RequestParam("saldo") double saldo,
            Model model) {
        try {
            usuarioService.registrarUsuarioConCuenta(usuario, numeroCuenta, banco, saldo);
            return "redirect:/login?registrado=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }

    // 4. Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(value = "registrado", required = false) Boolean registrado,
            Model model) {
        if (Boolean.TRUE.equals(registrado)) {
            model.addAttribute("mensajeExito", "¡Registro exitoso! Ya puedes iniciar sesión.");
        }
        return "login";
    }

    // 5. Procesar login — ahora usa validarLogin con STATE
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("dni") String dni,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        try {
            // STATE: validarLogin verifica que el estado permita el acceso
            Usuario usuario = usuarioService.validarLogin(dni, password);

            if (usuario != null) {
                session.setAttribute("usuarioLogueado", usuario);
                return "redirect:/perfil";
            } else {
                model.addAttribute("error", "El DNI o la contraseña son incorrectos.");
                return "login";
            }
        } catch (IllegalStateException e) {
            // El STATE lanzó excepción (cuenta suspendida o bloqueada)
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // 6. Panel de perfil
    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        // Recargamos el usuario desde BD para tener el estado actualizado
        Usuario usuarioActualizado = usuarioService.obtenerPerfilPorDni(usuario.getDni());
        if (usuarioActualizado == null) return "redirect:/login";

        session.setAttribute("usuarioLogueado", usuarioActualizado);
        model.addAttribute("usuario", usuarioActualizado);
        model.addAttribute("cuentas", usuarioService.obtenerCuentasPorUsuario(usuarioActualizado));
        model.addAttribute("notificaciones", notificacionService.obtenerUltimas10(usuarioActualizado));
        return "perfil";
    }

    // 7. Agregar cuenta
    @PostMapping("/agregar-cuenta")
    public String agregarCuenta(
            @RequestParam("banco") String banco,
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("saldo") double saldo,
            HttpSession session,
            Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        try {
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.setBanco(banco);
            nuevaCuenta.setNumeroCuenta(numeroCuenta);
            nuevaCuenta.setSaldo(saldo);
            usuarioService.agregarNuevaCuenta(usuario.getDni(), nuevaCuenta);
            return "redirect:/perfil";
        } catch (IllegalStateException e) {
            // STATE: usuario suspendido o bloqueado no puede agregar cuentas
            model.addAttribute("errorCuenta", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("cuentas", usuarioService.obtenerCuentasPorUsuario(usuario));
            return "perfil";
        }
    }

    // 8. Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}