package cl.evaluacion.transversal.usuario_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {

    @Email(message="Formato: nombre@empresa.dominio")
    @NotBlank(message="El correo es obligatorio")
    private String correo;

    @NotBlank(message="La contraseña es obligatoria")
    private String contrasenia;

    @NotBlank(message="El nombre es obligatorio")
    private String nombre;

    @NotBlank(message="El apellido es obligatorio")
    private String apellido;

    @NotBlank(message="El rol es obligatorio")
    private String rol;
}