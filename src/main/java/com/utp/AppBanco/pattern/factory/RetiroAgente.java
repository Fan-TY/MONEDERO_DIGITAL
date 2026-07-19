package com.utp.AppBanco.pattern.factory;

public class RetiroAgente implements OperacionFinanciera {

    @Override
    public String ejecutar(double monto, String referencia) {
        System.out.println("[FACTORY] Ejecutando RetiroAgente → S/." + monto + " | Agente: " + referencia);
        return "Retiro en agente procesado por S/." + monto;
    }

    @Override
    public String getTipo() {
        return "AGENTE";
    }
}
