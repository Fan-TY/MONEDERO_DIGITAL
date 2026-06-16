package com.utp.AppBanco.model;

import com.utp.AppBanco.pattern.prototype.PerfilConfiguracion;
import com.utp.AppBanco.pattern.state.EstadoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String tipoUsuario = "CLIENTE";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Transient
    private PerfilConfiguracion perfilConfiguracion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas;

    public boolean puedeOperar() {
        return estado != null && estado.puedeOperar();
    }

    public boolean puedeIniciarSesion() {
        return estado != null && estado.puedeIniciarSesion();
    }

    public void cambiarEstado(EstadoUsuario nuevoEstado) {
        System.out.println("[STATE] Usuario " + dni + ": " + this.estado + " → " + nuevoEstado);
        this.estado = nuevoEstado;
    }
}