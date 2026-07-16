package com.utp.AppBanco.pattern.factory;

public class OperacionFinancieraFactory {

    public enum TipoOperacion {
        RECARGA_TARJETA, RECARGA_BANCO, RETIRO_AGENTE
    }

    public static OperacionFinanciera crear(TipoOperacion tipo) {
        System.out.println("[FACTORY] Creando operación financiera de tipo: " + tipo);
        return switch (tipo) {
            case RECARGA_TARJETA -> new RecargaTarjeta();
            case RECARGA_BANCO -> new RecargaBanco();
            case RETIRO_AGENTE -> new RetiroAgente();
        };
    }
}
