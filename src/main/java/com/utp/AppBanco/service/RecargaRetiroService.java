package com.utp.AppBanco.service;

import com.utp.AppBanco.model.Agente;
import com.utp.AppBanco.model.Cuenta;
import com.utp.AppBanco.model.Recarga;
import com.utp.AppBanco.model.Retiro;
import com.utp.AppBanco.pattern.facade.RetiroFacade;
import com.utp.AppBanco.pattern.factory.OperacionFinancieraFactory;
import com.utp.AppBanco.pattern.observer.EventoOperacion;
import com.utp.AppBanco.pattern.observer.NotificadorOperaciones;
import com.utp.AppBanco.pattern.prototype.OperacionFrecuente;
import com.utp.AppBanco.pattern.singleton.DatabaseConnectionManager;
import com.utp.AppBanco.pattern.state.EstadoOperacion;
import com.utp.AppBanco.repository.AgenteRepository;
import com.utp.AppBanco.repository.CuentaRepository;
import com.utp.AppBanco.repository.RecargaRepository;
import com.utp.AppBanco.repository.RetiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RecargaRetiroService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private RecargaRepository recargaRepository;

    @Autowired
    private RetiroRepository retiroRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private RetiroFacade retiroFacade;

    @Autowired
    private NotificadorOperaciones notificador;

    @Autowired
    private DatabaseConnectionManager dbManager;

    private OperacionFrecuente ultimaOperacionFrecuente;

    @Transactional
    public Recarga recargarSaldo(String numeroCuenta, double monto, String tipoOrigen, String referencia) {

        System.out.println("[SINGLETON] Recarga iniciada. Estado BD: " + dbManager.getEstadoConexion());

        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe."));

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de recarga debe ser mayor a cero.");
        }

        OperacionFinancieraFactory.TipoOperacion tipo =
                "TARJETA".equalsIgnoreCase(tipoOrigen)
                        ? OperacionFinancieraFactory.TipoOperacion.RECARGA_TARJETA
                        : OperacionFinancieraFactory.TipoOperacion.RECARGA_BANCO;

        String referenciaLimpia = referencia == null ? "" : referencia.trim();

        String resultado = OperacionFinancieraFactory
                .crear(tipo)
                .ejecutar(monto, referenciaLimpia);

        Cuenta cuentaOrigenInterna = cuentaRepository.findById(referenciaLimpia).orElse(null);

        String nombrePropietarioOrigen = null;

        if (tipo == OperacionFinancieraFactory.TipoOperacion.RECARGA_BANCO
                && cuentaOrigenInterna == null) {

            throw new IllegalArgumentException(
                    "La cuenta de origen '" + referenciaLimpia +
                            "' no existe en el sistema.");
        }

        if (cuentaOrigenInterna != null) {

            if (cuentaOrigenInterna.getNumeroCuenta().equals(numeroCuenta)) {
                throw new IllegalArgumentException(
                        "La cuenta de origen no puede ser la misma.");
            }

            if (cuentaOrigenInterna.getSaldo() < monto) {
                throw new IllegalArgumentException(
                        "Saldo insuficiente en la cuenta origen.");
            }

            cuentaOrigenInterna.setSaldo(
                    cuentaOrigenInterna.getSaldo() - monto);

            cuentaRepository.save(cuentaOrigenInterna);

            nombrePropietarioOrigen =
                    cuentaOrigenInterna.getUsuario() != null
                            ? cuentaOrigenInterna.getUsuario().getNombre()
                            : null;
        }

        Recarga recarga = new Recarga();

        recarga.setCuenta(cuenta);
        recarga.setMonto(monto);
        recarga.setTipoOrigen(tipo.name());
        recarga.setReferenciaOrigen(referenciaLimpia);

        recarga.cambiarEstado(EstadoOperacion.VALIDADO);

        String comprobante =
                "REC-" + UUID.randomUUID().toString()
                        .substring(0, 8)
                        .toUpperCase();

        recarga.setComprobante(comprobante);

        cuenta.setSaldo(cuenta.getSaldo() + monto);

        cuentaRepository.save(cuenta);

        recarga.cambiarEstado(EstadoOperacion.ENTREGADO);

        recargaRepository.save(recarga);

        recarga.setTitularOrigen(nombrePropietarioOrigen);

        ultimaOperacionFrecuente =
                new OperacionFrecuente(
                        tipo.name(),
                        monto,
                        referenciaLimpia);

        String detalleOrigen = cuentaOrigenInterna != null
                ? " (Transferencia interna desde "
                + referenciaLimpia
                + (nombrePropietarioOrigen != null
                ? " - Titular: " + nombrePropietarioOrigen
                : "")
                + ")"
                : "";

        EventoOperacion evento = new EventoOperacion(
                cuenta.getUsuario(),
                "RECARGA",
                "Recarga de S/. "
                        + monto
                        + " acreditada en la cuenta "
                        + numeroCuenta
                        + detalleOrigen
                        + " | Comprobante: "
                        + comprobante,
                monto
        );

        notificador.notificarTodos(evento);

        return recarga;
    }

    @Transactional
    public Recarga repetirUltimaRecarga(String numeroCuenta) {

        if (ultimaOperacionFrecuente == null) {
            throw new IllegalStateException(
                    "No hay operaciones frecuentes registradas.");
        }

        OperacionFrecuente clon =
                ultimaOperacionFrecuente.clonarOperacion();

        String tipoOrigen =
                clon.getTipoOperacion().contains("TARJETA")
                        ? "TARJETA"
                        : "BANCO";

        return recargarSaldo(
                numeroCuenta,
                clon.getMontoHabitual(),
                tipoOrigen,
                clon.getReferenciaHabitual());
    }

    @Transactional
    public Retiro retirarEnAgente(
            String numeroCuenta,
            String codigoAgente,
            double monto,
            boolean biometriaValida) {

        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() ->
                        new IllegalArgumentException("La cuenta no existe."));

        Agente agente = agenteRepository.findById(codigoAgente)
                .orElseThrow(() ->
                        new IllegalArgumentException("El agente no existe."));

        if (!agente.isActivo()) {
            throw new IllegalStateException(
                    "El agente no está disponible.");
        }

        if (monto <= 0 || monto > cuenta.getSaldo()) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente o monto inválido.");
        }

        Retiro retiro =
                retiroFacade.procesarRetiro(
                        cuenta,
                        agente,
                        monto,
                        biometriaValida);

        cuenta.setSaldo(cuenta.getSaldo() - monto);

        cuentaRepository.save(cuenta);

        retiroRepository.save(retiro);

        EventoOperacion evento = new EventoOperacion(
                cuenta.getUsuario(),
                "RETIRO",
                "Retiro de S/. "
                        + monto
                        + " realizado en el agente "
                        + agente.getNombreComercial(),
                monto
        );

        notificador.notificarTodos(evento);

        return retiro;
    }

    public List<Recarga> obtenerRecargasPorCuenta(String numeroCuenta) {
        return recargaRepository.findByCuenta_NumeroCuentaOrderByFechaDesc(numeroCuenta);
    }

    public List<Retiro> obtenerRetirosPorCuenta(String numeroCuenta) {
        return retiroRepository.findByCuenta_NumeroCuentaOrderByFechaDesc(numeroCuenta);
    }

    public List<Agente> obtenerAgentesDisponibles() {
        return agenteRepository.findByActivoTrue();
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }
}