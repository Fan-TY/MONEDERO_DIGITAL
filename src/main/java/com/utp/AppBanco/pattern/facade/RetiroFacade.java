package com.utp.AppBanco.pattern.facade;

import com.utp.AppBanco.model.Agente;
import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Retiro;
import com.utp.AppBanco.pattern.adapter.AgenteExterno;
import com.utp.AppBanco.pattern.factory.OperacionFinancieraFactory;
import com.utp.AppBanco.pattern.observer.EventoOperacion;
import com.utp.AppBanco.pattern.observer.NotificadorOperaciones;
import com.utp.AppBanco.pattern.state.EstadoOperacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class RetiroFacade {
    @Autowired
    private AgenteExterno agenteExterno;
    @Autowired
    private NotificadorOperaciones notificador;
    public Retiro procesarRetiro(Cuenta cuenta, Agente agente, double monto, boolean biometriaValida) {
        System.out.println("[FACADE] Iniciando proceso simplificado de retiro...");

        Retiro retiro = new Retiro();
        retiro.setCuenta(cuenta);
        retiro.setAgente(agente);
        retiro.setMonto(monto);
        retiro.setEstado(EstadoOperacion.SOLICITADO);

        // 1. Validación biométrica (STATE: SOLICITADO -> VALIDADO)
        if (!biometriaValida) {
            throw new IllegalStateException("Validación biométrica fallida. No se puede continuar con el retiro.");
        }
        retiro.setValidacionBiometrica(true);
        retiro.cambiarEstado(EstadoOperacion.VALIDADO);

        // 2. ADAPTER: delega la autorización en el sistema externo del agente
        boolean autorizado = agenteExterno.procesarRetiro(agente.getCodigoAgente(), monto);
        if (!autorizado) {
            throw new IllegalStateException("El agente externo rechazó la operación de retiro.");
        }

        // 3. FACTORY: ejecuta la operación concreta
        OperacionFinancieraFactory.crear(OperacionFinancieraFactory.TipoOperacion.RETIRO_AGENTE)
                .ejecutar(monto, agente.getCodigoAgente());

        // 4. Generación de comprobante (STATE: VALIDADO -> ENTREGADO)
        String comprobante = "RET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        retiro.setComprobante(comprobante);
        retiro.cambiarEstado(EstadoOperacion.ENTREGADO);

        // 5. OBSERVER: notifica el evento a todos los canales suscritos
        notificador.notificarTodos(new EventoOperacion(
                cuenta.getUsuario(),
                "RETIRO_COMPLETADO",
                "Retiro de S/." + monto + " entregado en agente " + agente.getNombreComercial()
                        + " | Comprobante: " + comprobante,
                monto
        ));

        System.out.println("[FACADE] Proceso de retiro finalizado con comprobante: " + comprobante);
        return retiro;
    }
}
