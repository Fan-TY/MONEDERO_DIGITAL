package com.utp.AppBanco.pattern.state;

public enum EstadoOperacion {

    SOLICITADO("Solicitado", "warning", "bi-hourglass-split",
            "La operación fue registrada y está a la espera de validación."),

    VALIDADO("Validado", "info", "bi-patch-check-fill",
            "La operación fue validada correctamente (biometría / origen de fondos)."),

    ENTREGADO("Entregado", "success", "bi-check-circle-fill",
            "La operación se completó y el dinero fue entregado o acreditado.");

    private final String etiqueta;
    private final String colorBootstrap;
    private final String iconoBootstrap;
    private final String mensaje;

    EstadoOperacion(String etiqueta, String colorBootstrap, String iconoBootstrap, String mensaje) {
        this.etiqueta = etiqueta;
        this.colorBootstrap = colorBootstrap;
        this.iconoBootstrap = iconoBootstrap;
        this.mensaje = mensaje;
    }


    public EstadoOperacion siguiente() {
        return switch (this) {
            case SOLICITADO -> VALIDADO;
            case VALIDADO -> ENTREGADO;
            case ENTREGADO -> ENTREGADO;
        };
    }

    public String getEtiqueta() { return etiqueta; }
    public String getColorBootstrap() { return colorBootstrap; }
    public String getIconoBootstrap() { return iconoBootstrap; }
    public String getMensaje() { return mensaje; }
}
