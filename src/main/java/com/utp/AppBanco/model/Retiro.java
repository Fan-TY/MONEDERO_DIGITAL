package com.utp.AppBanco.model;

import com.utp.AppBanco.pattern.state.EstadoOperacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "retiros")
@Getter
@Setter
@NoArgsConstructor
public class Retiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_cuenta", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_agente", referencedColumnName = "codigo_agente", nullable = false)
    private Agente agente;

    @Column(nullable = false)
    private double monto;

    @Column(name = "validacion_biometrica", nullable = false)
    private boolean validacionBiometrica = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOperacion estado = EstadoOperacion.SOLICITADO;

    @Column(unique = true, length = 40)
    private String comprobante;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    public void cambiarEstado(EstadoOperacion nuevoEstado) {
        System.out.println("[STATE] Retiro " + id + ": " + this.estado + " → " + nuevoEstado);
        this.estado = nuevoEstado;
    }
}
