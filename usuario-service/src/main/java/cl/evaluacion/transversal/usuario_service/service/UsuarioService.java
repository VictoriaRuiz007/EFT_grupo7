package cl.evaluacion.transversal.usuario_service.service;

import cl.evaluacion.transversal.usuario_service.dto.UsuarioRequest;
import cl.evaluacion.transversal.usuario_service.exception.CorreoNoEncontradoException;
import cl.evaluacion.transversal.usuario_service.exception.UsuarioExisteException;
import cl.evaluacion.transversal.usuario_service.exception.UsuarioNoEncontradoException;
import cl.evaluacion.transversal.usuario_service.model.Usuario;
import cl.evaluacion.transversal.usuario_service.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        log.info("Listando a todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id){
        log.info("Buscando usuario con id : {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(()-> new UsuarioNoEncontradoException("Usuario no encontrado"));
    }

    public Usuario findByCorreo(String correo){
        log.info("Buscando usuario con correo : {}", correo);
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(()-> new CorreoNoEncontradoException("Correo no encontrado"));
    }

    public Usuario save(UsuarioRequest ur){
        log.info("Guardando usuario: {}", ur);
        if(usuarioRepository.findByCorreo(ur.getCorreo()).isPresent()){
            throw new UsuarioExisteException("Ya existe un usuario con el mismo correo");
        }

        Usuario u = new Usuario();
        u.setCorreo(ur.getCorreo());
        u.setContrasenia(ur.getContrasenia());
        u.setNombre(ur.getNombre());
        u.setApellido(ur.getApellido());
        u.setRol("CLIENTE");
        return usuarioRepository.save(u);
    }

    public void deleteById(Long id){
        log.info("Eliminando usuario con id : {}", id);
        if(!usuarioRepository.existsById(id)){
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
