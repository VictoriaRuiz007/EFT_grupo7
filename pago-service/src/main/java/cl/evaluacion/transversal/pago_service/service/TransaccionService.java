package cl.evaluacion.transversal.pago_service.service;

import cl.evaluacion.transversal.pago_service.dto.TransaccionRequest;
import cl.evaluacion.transversal.pago_service.exception.PedidoNoEncontradoException;
import cl.evaluacion.transversal.pago_service.exception.TransaccionNoEncontradaException;
import cl.evaluacion.transversal.pago_service.model.Transaccion;
import cl.evaluacion.transversal.pago_service.repository.TransaccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

    private static final Logger log = LoggerFactory.getLogger(TransaccionService.class);

    @Autowired
    private TransaccionRepository transaccionRepository;

    public Transaccion save(TransaccionRequest tr) {
        log.info("Guardando transaccion : {}", tr);
        Transaccion t = new Transaccion();
        t.setMonto(tr.getMonto());
        t.setEstado(tr.getEstado());
        t.setMetodoPago(tr.getMetodoPago());
        t.setFecha(tr.getFecha());
        t.setIdPedido(tr.getIdPedido());
        return transaccionRepository.save(t);
    }

    public Transaccion findById(Long id) {
        log.info("Buscando transaccion por el id : {}", id);
        return transaccionRepository.findById(id)
                .orElseThrow(()-> new TransaccionNoEncontradaException("Transaccion no encontrada"));
    }

    public List<Transaccion> findAll() {
        log.info("Listando todas las transacciones:");
        return transaccionRepository.findAll();
    }

    public void deleteById(Long id) {
        log.info("Eliminando transaccion con id : {}", id);
        if(!transaccionRepository.existsById(id)) {
            throw new TransaccionNoEncontradaException("Transaccion no encontrada");
        }
        transaccionRepository.deleteById(id);
    }

    public Transaccion findByIdPedido(Long idPedido) {
        log.info("Buscando transaccion por el pedido con id : {}", idPedido);
        return transaccionRepository.findByIdPedido(idPedido)
                .orElseThrow(()-> new PedidoNoEncontradoException("Pedido no encontrado"));
    }
}
