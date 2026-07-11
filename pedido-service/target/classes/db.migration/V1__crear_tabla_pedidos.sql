CREATE TABLE pedidos (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         id_usuario INT NOT NULL,
                         total DOUBLE NOT NULL,
                         codigo_promocion VARCHAR(50),
                         detalle_productos VARCHAR(255) NOT NULL,
                         fecha_pedido DATE NOT NULL
);