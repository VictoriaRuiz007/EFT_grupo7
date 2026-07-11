package cl.evaluacion.transversal.catalogo_service.service;

import cl.evaluacion.transversal.catalogo_service.dto.VideojuegoRequest;
import cl.evaluacion.transversal.catalogo_service.exception.ProductoExistenteException;
import cl.evaluacion.transversal.catalogo_service.exception.ProductoNoEncontradoException;
import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import cl.evaluacion.transversal.catalogo_service.repository.VideojuegoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideojuegoService {

    private static final Logger log = LoggerFactory.getLogger(VideojuegoService.class);

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    public List<Videojuego> listAll(){
        log.info("Listando todos los videojuegos");
        return videojuegoRepository.findAll();
    }

    public Videojuego findById(Long id){
        log.info("Buscando videojuego por id={}", id);
        return videojuegoRepository.findById(id)
                .orElseThrow(()-> new ProductoNoEncontradoException("Producto no encontrado"));
    }

    public Videojuego save(VideojuegoRequest vr){
        log.info("Guardando videojuego {}, con id: {}", vr.getTitulo());
        List<Videojuego> videojuegos = listAll();
        boolean existe = videojuegos.stream()
                .anyMatch(v -> v.getTitulo().equalsIgnoreCase(vr.getTitulo()));

        if(existe){
            throw new ProductoExistenteException("Producto ya existe");
        }

        Videojuego v = new Videojuego();
        v.setTitulo(vr.getTitulo());
        v.setCategoria(vr.getCategoria());
        v.setStock(vr.getStock());
        v.setPrecio(vr.getPrecio());
        v.setPlataforma(vr.getPlataforma());

        return videojuegoRepository.save(v);
    }

    public void delete(Long id){
        log.info("Eliminando videojuego de id: {}", id);
        if(!videojuegoRepository.existsById(id)){
            throw new ProductoNoEncontradoException("Producto no encontrado");
        }

        videojuegoRepository.deleteById(id);
    }
}
