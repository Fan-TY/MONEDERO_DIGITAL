package com.utp.AppBanco.pattern.prototype;

public class PerfilConfiguracion implements Cloneable {

    private String monedaPreferida; 
    private String idiomaInterfaz; 
    private boolean notificacionesEmail;
    private boolean notificacionesSms;
    private int limiteDiarioTransferencia;
    private String temaVisual;        

    private PerfilConfiguracion() {}

    public static PerfilConfiguracion crearConfiguracionBase() {
        PerfilConfiguracion base = new PerfilConfiguracion();
        base.monedaPreferida = "PEN";
        base.idiomaInterfaz = "es";
        base.notificacionesEmail = true;
        base.notificacionesSms = false;
        base.limiteDiarioTransferencia = 2000;
        base.temaVisual = "oscuro";
        return base;
    }

    public PerfilConfiguracion clonarPara(String nombreUsuario) {
        try {
            PerfilConfiguracion clon = (PerfilConfiguracion) super.clone();
            System.out.println("[PROTOTYPE] Configuración clonada para usuario: " + nombreUsuario);
            System.out.println("  → Moneda: " + clon.monedaPreferida + " | Límite: S/." + clon.limiteDiarioTransferencia);
            return clon;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar configuración de perfil", e);
        }
    }


    public String getMonedaPreferida() { return monedaPreferida; }
    public void setMonedaPreferida(String monedaPreferida) { this.monedaPreferida = monedaPreferida; }

    public String getIdiomaInterfaz() { return idiomaInterfaz; }
    public void setIdiomaInterfaz(String idiomaInterfaz) { this.idiomaInterfaz = idiomaInterfaz; }

    public boolean isNotificacionesEmail() { return notificacionesEmail; }
    public void setNotificacionesEmail(boolean notificacionesEmail) { this.notificacionesEmail = notificacionesEmail; }

    public boolean isNotificacionesSms() { return notificacionesSms; }
    public void setNotificacionesSms(boolean notificacionesSms) { this.notificacionesSms = notificacionesSms; }

    public int getLimiteDiarioTransferencia() { return limiteDiarioTransferencia; }
    public void setLimiteDiarioTransferencia(int limiteDiarioTransferencia) { this.limiteDiarioTransferencia = limiteDiarioTransferencia; }

    public String getTemaVisual() { return temaVisual; }
    public void setTemaVisual(String temaVisual) { this.temaVisual = temaVisual; }

    @Override
    public String toString() {
        return "PerfilConfiguracion{moneda='" + monedaPreferida + "', idioma='" + idiomaInterfaz +
                "', email=" + notificacionesEmail + ", sms=" + notificacionesSms +
                ", limiteTransferencia=" + limiteDiarioTransferencia + ", tema='" + temaVisual + "'}";
    }
}