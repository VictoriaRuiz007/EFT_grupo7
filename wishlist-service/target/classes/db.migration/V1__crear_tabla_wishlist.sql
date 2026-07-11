CREATE TABLE lista_deseos (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              id_usuario INT NOT NULL,
                              id_producto INT NOT NULL,
                              fecha_agregado DATE NOT NULL
);