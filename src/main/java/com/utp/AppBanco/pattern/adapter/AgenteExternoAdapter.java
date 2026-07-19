package com.utp.AppBanco.pattern.adapter;

import org.springframework.stereotype.Component;


@Component
public class AgenteExternoAdapter implements AgenteExterno {

    private final SistemaCajeroExterno sistemaExterno = new SistemaCajeroExterno();

    @Override
    public boolean procesarRetiro(String codigoAgente, double monto) {
        System.out.println("[ADAPTER] Adaptando llamada interna → sistema externo de agentes/cajeros");
        return sistemaExterno.autorizarRetiroExterno(codigoAgente, monto);
    }
}
