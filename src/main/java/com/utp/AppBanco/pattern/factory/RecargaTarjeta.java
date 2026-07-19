package com.utp.AppBanco.pattern.factory;

public class RecargaTarjeta implements OperacionFinanciera {

    @Override
    public String ejecutar(double monto, String referencia) {
        System.out.println("[FACTORY] Ejecutando RecargaTarjeta → S/." + monto + " | Tarjeta: " + referencia);
        return "Recarga vía tarjeta procesada por S/." + monto;
    }

    @Override
    public String getTipo() {
        return "TARJETA";
    }
}
