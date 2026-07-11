package cl.evaluacion.transversal.wishlist_service.service;

import cl.evaluacion.transversal.wishlist_service.dto.DeseoRequest;
import cl.evaluacion.transversal.wishlist_service.exception.WishlistNoExisteException;
import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import cl.evaluacion.transversal.wishlist_service.repository.DeseoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeseoService {

    private static final Logger log = LoggerFactory.getLogger(DeseoService.class);

    @Autowired
    private DeseoRepository deseoRepository;

    public Deseo listByUsuario(Long idUsuario){
        log.info("Listando wishlist de usuario con id : {}", idUsuario);
        return deseoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(()-> new WishlistNoExisteException("Este usuario o wishlist no existe"));
    }

    public Deseo save(DeseoRequest dr){
        log.info("Guardando deseo en la wishlist: {}", dr);

        if(!deseoRepository.findByIdUsuario(dr.getIdUsuario()).isPresent()){
            throw new WishlistNoExisteException("Usuario no encontrado");
        }

        Deseo d = new Deseo();
        d.setIdUsuario(dr.getIdUsuario());
        d.setIdProducto(dr.getIdProducto());
        d.setFechaAgregado(dr.getFechaAgregado());
        return deseoRepository.save(d);
    }

    public void deleteByUsuario(Long idUsuario){
        log.info("Eliminando wishlist de usuario con id : {}", idUsuario);
        if(!deseoRepository.findByIdUsuario(idUsuario).isPresent()){
            throw new WishlistNoExisteException("Usuario no encontrado");
        }
        deseoRepository.deleteByIdUsuario(idUsuario);
    }
}
