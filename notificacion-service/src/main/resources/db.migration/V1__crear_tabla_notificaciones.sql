CREATE TABLE notificaciones (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                id_usuario INT NOT NULL,
                                tipo VARCHAR(50) NOT NULL,
                                mensaje TEXT NOT NULL,
                                fecha_envio DATETIME NOT NULL
);