package com.utp.AppBanco.pattern.factory;

public class RecargaBanco implements OperacionFinanciera {

    @Override
    public String ejecutar(double monto, String referencia) {
        System.out.println("[FACTORY] Ejecutando RecargaBanco → S/." + monto + " | Cuenta origen: " + referencia);
        return "Recarga vía transferencia bancaria procesada por S/." + monto;
    }

    @Override
    public String getTipo() {
        return "BANCO";
    }
}
