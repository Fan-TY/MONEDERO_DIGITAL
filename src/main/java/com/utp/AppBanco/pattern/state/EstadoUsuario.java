package com.utp.AppBanco.pattern.state;

public enum EstadoUsuario {

    ACTIVO("Activo", "success", "bi-check-circle-fill",
            "Tu cuenta está activa y operativa."),

    SUSPENDIDO("Suspendido", "warning", "bi-pause-circle-fill",
            "Tu cuenta está suspendida temporalmente. Contacta a soporte."),

    BLOQUEADO("Bloqueado", "danger", "bi-x-circle-fill",
            "Tu cuenta ha sido bloqueada. Comunícate con soporte inmediatamente.");

    private final String etiqueta;
    private final String colorBootstrap;
    private final String iconoBootstrap;
    private final String mensaje; 

    EstadoUsuario(String etiqueta, String colorBootstrap, String iconoBootstrap, String mensaje) {
        this.etiqueta = etiqueta;
        this.colorBootstrap = colorBootstrap;
        this.iconoBootstrap = iconoBootstrap;
        this.mensaje = mensaje;
    }

    public boolean puedeOperar() {
        return this == ACTIVO;
    }

    public boolean puedeIniciarSesion() {
        return this == ACTIVO || this == SUSPENDIDO;
    }


    public String getEtiqueta() { return etiqueta; }
    public String getColorBootstrap() { return colorBootstrap; }
    public String getIconoBootstrap() { return iconoBootstrap; }
    public String getMensaje() { return mensaje; }
}