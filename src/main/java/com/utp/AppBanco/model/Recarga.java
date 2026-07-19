package com.utp.AppBanco.model;

import com.utp.AppBanco.pattern.state.EstadoOperacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "recargas")
@Getter
@Setter
@NoArgsConstructor
public class Recarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_cuenta", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuenta;

    @Column(name = "tipo_origen", nullable = false, length = 20)
    private String tipoOrigen;

    @Column(nullable = false)
    private double monto;

    @Column(length = 40)
    private String referenciaOrigen;


    @Transient
    private String titularOrigen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOperacion estado = EstadoOperacion.SOLICITADO;

    @Column(nullable = false, unique = true, length = 40)
    private String comprobante;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    public void cambiarEstado(EstadoOperacion nuevoEstado) {
        System.out.println("[STATE] Recarga " + id + ": " + this.estado + " → " + nuevoEstado);
        this.estado = nuevoEstado;
    }
}
