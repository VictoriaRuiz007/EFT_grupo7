CREATE TABLE videojuegos (
                             id LONG PRIMARY KEY AUTO_INCREMENT,
                             titulo VARCHAR(100) NOT NULL UNIQUE,
                             plataforma VARCHAR(50) NOT NULL,
                             stock INT(100) NOT NULL,
                             categoria VARCHAR(50) NOT NULL,
                             precio DOUBLE(6,3) NOT NULL
);