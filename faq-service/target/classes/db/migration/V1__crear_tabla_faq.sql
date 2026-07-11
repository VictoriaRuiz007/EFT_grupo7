CREATE TABLE preguntas_frecuentes (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      pregunta VARCHAR(255) NOT NULL,
                                      respuesta TEXT NOT NULL,
                                      categoria VARCHAR(100) NOT NULL
);