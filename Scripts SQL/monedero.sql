-- Creacion de base de datos
CREATE DATABASE IF NOT EXISTS monedero;
USE monedero;

-- Seleccion de tablas
SELECT * FROM cuenta_bancaria;
SELECT * FROM usuarios;

-- Estados
UPDATE usuarios SET estado = 'SUSPENDIDO' WHERE dni = '12345678';
UPDATE usuarios SET estado = 'BLOQUEADO' WHERE dni = '12345678';
UPDATE usuarios SET estado = 'ACTIVO' WHERE dni = '12345678';

-- Roles
UPDATE usuarios SET tipo_usuario = 'CLIENTE' WHERE dni = '12345678';
UPDATE usuarios SET tipo_usuario = 'COMERCIO' WHERE dni = '12345678';
UPDATE usuarios SET tipo_usuario = 'ADMIN'    WHERE dni = '12345678';

-- Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(255)  NOT NULL,
    dni           VARCHAR(8)    NOT NULL UNIQUE,
    password      VARCHAR(255)  NOT NULL,
    tipo_usuario  VARCHAR(20)   NOT NULL DEFAULT 'CLIENTE',
    estado        VARCHAR(20)   NOT NULL DEFAULT 'ACTIVO'
);

-- Tabla Cuenta Bancaria
CREATE TABLE IF NOT EXISTS cuenta_bancaria (
    numero_cuenta VARCHAR(30)  NOT NULL PRIMARY KEY,
    banco         VARCHAR(50)  NOT NULL,
    saldo         DOUBLE       NOT NULL,
    usuario_dni   VARCHAR(8)   NOT NULL,
    CONSTRAINT fk_usuario_cuenta 
        FOREIGN KEY (usuario_dni) 
        REFERENCES usuarios(dni)
);


-- MÓDULO 5 — RECARGAS Y RETIROS


-- Tabla Agente
CREATE TABLE IF NOT EXISTS agentes (
    codigo_agente     VARCHAR(20)   NOT NULL PRIMARY KEY,
    nombre_comercial  VARCHAR(100)  NOT NULL,
    ubicacion         VARCHAR(150)  NOT NULL,
    saldo_disponible  DOUBLE        NOT NULL DEFAULT 0,
    activo            BOOLEAN       NOT NULL DEFAULT TRUE
);

-- Tabla Recarga
CREATE TABLE IF NOT EXISTS recargas (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta     VARCHAR(30)   NOT NULL,
    tipo_origen       VARCHAR(20)   NOT NULL,
    referencia_origen VARCHAR(40),
    monto             DOUBLE        NOT NULL,
    estado            VARCHAR(20)   NOT NULL DEFAULT 'SOLICITADO',
    comprobante       VARCHAR(40)   NOT NULL UNIQUE,
    fecha             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recarga_cuenta
        FOREIGN KEY (numero_cuenta)
        REFERENCES cuenta_bancaria(numero_cuenta)
);

-- Tabla Retiro
CREATE TABLE IF NOT EXISTS retiros (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta          VARCHAR(30)  NOT NULL,
    codigo_agente          VARCHAR(20)  NOT NULL,
    monto                  DOUBLE       NOT NULL,
    validacion_biometrica  BOOLEAN      NOT NULL DEFAULT FALSE,
    estado                 VARCHAR(20)  NOT NULL DEFAULT 'SOLICITADO',
    comprobante            VARCHAR(40)  UNIQUE,
    fecha                  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_retiro_cuenta
        FOREIGN KEY (numero_cuenta)
        REFERENCES cuenta_bancaria(numero_cuenta),
    CONSTRAINT fk_retiro_agente
        FOREIGN KEY (codigo_agente)
        REFERENCES agentes(codigo_agente)
);

-- Datos de ejemplo para poder probar los retiros (Hibernate no los genera solo)
INSERT IGNORE INTO agentes (codigo_agente, nombre_comercial, ubicacion, saldo_disponible, activo) VALUES
('AG-001', 'Bodega San Martín', 'Av. Los Álamos 123, Lima', 15000, TRUE),
('AG-002', 'Farmacia Central', 'Jr. Union 456, Lima', 8000, TRUE),
('AG-003', 'Minimarket El Sol', 'Av. Grau 789, Lima', 5000, TRUE);


DROP PROCEDURE IF EXISTS sp_registrar_usuario_con_cuenta;

-- Procedure de registro
DELIMITER //
CREATE PROCEDURE sp_registrar_usuario_con_cuenta(
    IN p_nombre VARCHAR(255),
    IN p_dni VARCHAR(8),
    IN p_password VARCHAR(255),
    IN p_numero_cuenta VARCHAR(30),
    IN p_banco VARCHAR(50),
    IN p_saldo DOUBLE
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    INSERT INTO usuarios (nombre, dni, password, estado, tipo_usuario) 
    VALUES (p_nombre, p_dni, p_password, 'ACTIVO', 'CLIENTE');
    
    INSERT INTO cuenta_bancaria (numero_cuenta, banco, saldo, usuario_dni) 
    VALUES (p_numero_cuenta, p_banco, p_saldo, p_dni);
    
    COMMIT;
END //
DELIMITER ;





