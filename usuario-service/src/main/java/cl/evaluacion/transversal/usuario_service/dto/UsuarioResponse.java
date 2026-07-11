package cl.evaluacion.transversal.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String correo;
    private String contrasenia;
    private String nombre;
    private String apellido;
    private String rol;
}