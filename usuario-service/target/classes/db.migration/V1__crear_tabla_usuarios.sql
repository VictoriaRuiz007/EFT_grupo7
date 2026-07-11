CREATE TABLE usuarios (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          rol VARCHAR(20) NOT NULL,
);