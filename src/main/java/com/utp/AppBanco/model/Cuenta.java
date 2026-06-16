package com.utp.AppBanco.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuenta_bancaria")
public class Cuenta {

    @Id
    @Column(name = "numero_cuenta", length = 30)
    private String numeroCuenta;

    @Column(nullable = false, length = 50)
    private String banco;

    @Column(nullable = false)
    private double saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_dni", referencedColumnName = "dni", nullable = false)
    private Usuario usuario;
}