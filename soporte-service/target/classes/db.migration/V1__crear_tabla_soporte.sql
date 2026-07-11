CREATE TABLE tickets_soporte (
                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                 id_usuario INT NOT NULL,
                                 asunto VARCHAR(150) NOT NULL,
                                 descripcion TEXT NOT NULL,
                                 estado VARCHAR(50) NOT NULL,
                                 fecha_creacion DATE NOT NULL
);