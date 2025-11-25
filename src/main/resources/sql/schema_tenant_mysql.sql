-- =========================================================
--  TABLA DEMO PARA UN SISTEMA MULTITENANT
--  Aquí solo se muestra una estructura simple.
--  En el proyecto real, aquí debe ir TODO el script completo
--  de creación de la base de datos para cada tenant.
-- =========================================================
CREATE TABLE IF NOT EXISTS `tablademo` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `nombre` VARCHAR(100) DEFAULT NULL,
   `descripcion` VARCHAR(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
);

-- Nota:
-- Esta tabla es solo un ejemplo simplificado.
-- En el entorno real multitenant, aquí deben incluirse todas las tablas,
-- relaciones, índices y configuraciones necesarias para cada tenant.
