package com.utp.AppBanco.pattern.prototype;


public class OperacionFrecuente implements Cloneable {

    private String tipoOperacion;
    private double montoHabitual;
    private String referenciaHabitual;

    public OperacionFrecuente(String tipoOperacion, double montoHabitual, String referenciaHabitual) {
        this.tipoOperacion = tipoOperacion;
        this.montoHabitual = montoHabitual;
        this.referenciaHabitual = referenciaHabitual;
    }

    public OperacionFrecuente clonarOperacion() {
        try {
            OperacionFrecuente clon = (OperacionFrecuente) super.clone();
            System.out.println("[PROTOTYPE] Operación clonada → Tipo: " + clon.tipoOperacion
                    + " | Monto habitual: S/." + clon.montoHabitual);
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar operación frecuente", e);
        }
    }

    public String getTipoOperacion() { return tipoOperacion; }
    public void setTipoOperacion(String tipoOperacion) { this.tipoOperacion = tipoOperacion; }

    public double getMontoHabitual() { return montoHabitual; }
    public void setMontoHabitual(double montoHabitual) { this.montoHabitual = montoHabitual; }

    public String getReferenciaHabitual() { return referenciaHabitual; }
    public void setReferenciaHabitual(String referenciaHabitual) { this.referenciaHabitual = referenciaHabitual; }
}
