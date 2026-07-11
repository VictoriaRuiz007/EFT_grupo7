CREATE TABLE promociones (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             codigo VARCHAR(50) UNIQUE NOT NULL,
                             porcentaje_descuento DOUBLE NOT NULL,
                             fecha_expiracion DATE,
                             activo BOOLEAN DEFAULT TRUE
);