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





