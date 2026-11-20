-- ============================================================
--  FRIIADMI - Script de base de datos (MySQL 8+)
--  Opcional: la aplicación también crea las tablas sola
--  (spring.jpa.hibernate.ddl-auto=update).
-- ============================================================

CREATE DATABASE IF NOT EXISTS friiadmi
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE friiadmi;

-- ------------------------------------------------------------
--  Configuración del torneo (una sola fila, id = 1)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS configuracion (
    id                    BIGINT        NOT NULL,
    nombre_torneo         VARCHAR(120)  NOT NULL,
    comision_porcentaje   DECIMAL(5,2)  NOT NULL DEFAULT 20.00,
    cuota_inscripcion     DECIMAL(10,2) NOT NULL DEFAULT 10.00,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Equipos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS equipos (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(80)  NOT NULL,
    puntos_totales  INT          NOT NULL DEFAULT 0,
    total_kills     INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_equipos_nombre (nombre)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Jugadores (capitán = jugador con capitan = 1; máx. 5 por equipo)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jugadores (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(100)  NOT NULL,
    id_free_fire   VARCHAR(20)   NOT NULL,
    celular        VARCHAR(20)   NOT NULL,
    equipo_id      BIGINT        NULL,
    capitan        TINYINT(1)    NOT NULL DEFAULT 0,
    monto          DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    fecha_pago     DATE          NULL,
    estado_pago    VARCHAR(20)   NOT NULL DEFAULT 'PENDIENTE',
    metodo_pago    VARCHAR(20)   NULL,
    comprobante    VARCHAR(255)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_jugadores_id_free_fire (id_free_fire),
    KEY idx_jugadores_equipo (equipo_id),
    CONSTRAINT fk_jugadores_equipo FOREIGN KEY (equipo_id)
        REFERENCES equipos (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Partidas
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS partidas (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    fecha   DATE         NOT NULL,
    mapa    VARCHAR(40)  NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Resultados por partida (puntos = puntos por posición + kills)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS resultados (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    partida_id  BIGINT NOT NULL,
    equipo_id   BIGINT NOT NULL,
    posicion    INT    NOT NULL,
    kills       INT    NOT NULL DEFAULT 0,
    puntos      INT    NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_resultados_partida_equipo (partida_id, equipo_id),
    CONSTRAINT fk_resultados_partida FOREIGN KEY (partida_id)
        REFERENCES partidas (id) ON DELETE CASCADE,
    CONSTRAINT fk_resultados_equipo FOREIGN KEY (equipo_id)
        REFERENCES equipos (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
--  Datos iniciales
-- ------------------------------------------------------------
INSERT INTO configuracion (id, nombre_torneo, comision_porcentaje, cuota_inscripcion)
VALUES (1, 'Torneo FRIIADMI', 20.00, 10.00)
ON DUPLICATE KEY UPDATE id = id;
