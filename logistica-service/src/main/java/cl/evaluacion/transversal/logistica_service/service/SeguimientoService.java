package cl.evaluacion.transversal.logistica_service.service;

import cl.evaluacion.transversal.logistica_service.dto.LogisticaRequest;
import cl.evaluacion.transversal.logistica_service.exception.CodigoNoEncontradoException;
import cl.evaluacion.transversal.logistica_service.exception.SeguimientoExisteException;
import cl.evaluacion.transversal.logistica_service.exception.SeguimientoNoEncontradoException;
import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import cl.evaluacion.transversal.logistica_service.repository.SeguimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeguimientoService {

    private static final Logger log = LoggerFactory.getLogger(SeguimientoService.class);

    @Autowired
    private SeguimientoRepository seguimientoRepository;

    public List<Seguimiento> findAll() {
        log.info("Mostrando todas las Seguimientos:");
        return seguimientoRepository.findAll();
    }

    public Seguimiento findByCodigo(String codigo){
        log.info("Buscando seguimiento por codigo : {}", codigo);
        return seguimientoRepository.findByCodigo(codigo)
                .orElseThrow(()->new CodigoNoEncontradoException("No se encontro el codigo"));

    }

    public Seguimiento save(LogisticaRequest lr) {
        log.info("Guardando seguimiento : {}", lr);
        List<Seguimiento> seguimientos = findAll();
        boolean existe = seguimientos.stream()
                .anyMatch(l -> l.getCodigoSeguimiento().equalsIgnoreCase(lr.getCodigoSeguimiento()));
        if(existe){
            throw new SeguimientoExisteException("Seguimiento ya existe");
        }
        Seguimiento l = new Seguimiento();
        l.setCodigoSeguimiento(lr.getCodigoSeguimiento());
        l.setEstado(lr.getEstado());
        l.setEmpresaEnvio(lr.getEmpresaEnvio());
        l.setDireccionDestino(lr.getDireccionDestino());
        l.setFechaEstimada(lr.getFechaEstimada());

        return seguimientoRepository.save(l);
    }

    public Seguimiento findById(Long id){
        log.info("Buscando seguimiento por id : {}", id);
        return seguimientoRepository.findById(id)
                .orElseThrow(()-> new SeguimientoNoEncontradoException("No se encontro el seguimiento"));
    }

    public void deleteById(Long id){
        log.info("Eliminando seguimiento por id : {}", id);
        if(!seguimientoRepository.existsById(id)){
            throw new SeguimientoNoEncontradoException("No existe seguimiento");
        }
        seguimientoRepository.deleteById(id);
    }
}
