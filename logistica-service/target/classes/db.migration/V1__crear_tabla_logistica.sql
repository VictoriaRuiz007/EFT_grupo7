CREATE TABLE despachos (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           codigo_seguimiento VARCHAR(12) NOT NULL UNIQUE,
                           empresa_envio VARCHAR(100) NOT NULL,
                           estado VARCHAR(50) NOT NULL,
                           direccion_destino VARCHAR(255) NOT NULL,
                           fecha_estimada DATE NOT NULL,
                           id_pedido INT NOT NULL
);