CREATE TABLE pagos (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       id_pedido INT NOT NULL,
                       metodo VARCHAR(50) NOT NULL,
                       monto DOUBLE NOT NULL,
                       estado VARCHAR(50) NOT NULL,
                       fecha DATETIME NOT NULL
);