package com.utp.AppBanco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agente {

    @Id
    @Column(name = "codigo_agente", length = 20)
    private String codigoAgente;

    @Column(name = "nombre_comercial", nullable = false, length = 100)
    private String nombreComercial;

    @Column(nullable = false, length = 150)
    private String ubicacion;

    @Column(name = "saldo_disponible", nullable = false)
    private double saldoDisponible;

    @Column(nullable = false)
    private boolean activo = true;
}
