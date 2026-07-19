package com.utp.AppBanco.pattern.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnectionManager {

    private static volatile DatabaseConnectionManager instancia;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private int totalConexionesAbiertas = 0;

    private DatabaseConnectionManager() {}

    public static DatabaseConnectionManager getInstance() {
        if (instancia == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instancia == null) {
                    instancia = new DatabaseConnectionManager();
                }
            }
        }
        return instancia;
    }

    @PostConstruct
    private void init() {
        instancia = this;
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  [SINGLETON] DatabaseConnectionManager iniciado.");
        System.out.println("  Instancia única creada: " + this.hashCode());
        System.out.println("  Base de datos: " + url);
        System.out.println("═══════════════════════════════════════════════════");
    }

    public Connection obtenerConexion() throws SQLException {
        totalConexionesAbiertas++;
        System.out.println("[SINGLETON] Conexión #" + totalConexionesAbiertas + " solicitada desde instancia " + this.hashCode());
        return DriverManager.getConnection(url, username, password);
    }

    public String getEstadoConexion() {
    return "Instancia única [" + this.hashCode() + "] | Pool HikariCP activo | BD: " + url;
}

    public int getTotalConexionesAbiertas() {
        return totalConexionesAbiertas;
    }
}