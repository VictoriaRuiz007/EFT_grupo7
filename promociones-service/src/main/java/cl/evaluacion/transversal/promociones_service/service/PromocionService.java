package cl.evaluacion.transversal.promociones_service.service;

import cl.evaluacion.transversal.promociones_service.dto.PromocionRequest;
import cl.evaluacion.transversal.promociones_service.exception.PromocionExisteException;
import cl.evaluacion.transversal.promociones_service.exception.PromocionNoEncontradaException;
import cl.evaluacion.transversal.promociones_service.model.Promocion;
import cl.evaluacion.transversal.promociones_service.repository.PromocionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromocionService {

    private static final Logger log = LoggerFactory.getLogger(PromocionService.class);

    @Autowired
    private PromocionRepository promocionRepository;

    public List<Promocion> findAll() {
        log.info("Listando todas las promociones: ");
        return promocionRepository.findAll();
    }

    public Promocion save(PromocionRequest pr) {
        log.info("Agregando promocion: {}", pr);
        if(promocionRepository.findByCodigo(pr.getCodigo()).isPresent()) {
            throw new PromocionExisteException("Ya existe esta promocion");
        }

        Promocion p = new Promocion();
        p.setCodigo(pr.getCodigo());
        p.setActivo(true);
        p.setFechaExpiracion(pr.getFechaExpiracion());
        p.setPorcentajeDescuento(pr.getPorcentajeDescuento());
        return promocionRepository.save(p);
    }

    public Promocion findById(Long id) {
        log.info("Buscando promocion con id: {}", id);
        return promocionRepository.findById(id)
                .orElseThrow(()-> new PromocionNoEncontradaException("Promocion no encontrado"));
    }

    public Promocion findByCodigo(String codigo) {
        log.info("Buscando promocion con codigo: {}", codigo);
        return promocionRepository.findByCodigo(codigo)
                .orElseThrow(()-> new PromocionNoEncontradaException("No existe la promocion"));
    }

    public void deleteById(Long id) {
        log.info("Eliminando promocion con id : {}", id);
        if(!promocionRepository.existsById(id)) {
            throw new PromocionNoEncontradaException("Promocion no encontrado");
        }
        promocionRepository.deleteById(id);
    }
}
