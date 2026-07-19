package com.utp.AppBanco.pattern.adapter;


public class SistemaCajeroExterno {

    public boolean autorizarRetiroExterno(String codigoTerminal, double importe) {
        System.out.println("[ADAPTER] Sistema externo del cajero/agente autorizando importe: S/." + importe
                + " en terminal: " + codigoTerminal);

        return importe > 0 && importe <= 5000;
    }
}
