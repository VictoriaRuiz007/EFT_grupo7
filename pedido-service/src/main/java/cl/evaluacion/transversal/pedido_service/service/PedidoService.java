package cl.evaluacion.transversal.pedido_service.service;

import cl.evaluacion.transversal.pedido_service.dto.PedidoRequest;
import cl.evaluacion.transversal.pedido_service.exception.PedidoNoEncontradoException;
import cl.evaluacion.transversal.pedido_service.model.Pedido;
import cl.evaluacion.transversal.pedido_service.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido findById(Long id) {
        log.info("Buscando pedido por el id : {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(()-> new PedidoNoEncontradoException("Pedido no encontrado"));
    }

    public List<Pedido> findAll() {
        log.info("Listando todos los pedidos");
        return pedidoRepository.findAll();
    }

    public Pedido create(PedidoRequest pr) {
        log.info("Creando pedido : {}", pr);

        Pedido p = new Pedido();
        p.setFechaPedido(pr.getFechaPedido());
        p.setCodigoPromocion(pr.getCodigoPromocion());
        p.setTotal(pr.getTotal());
        p.setIdUsuario(pr.getIdUsuario());
        p.setDetalleProductos(pr.getDetalleProductos());
        return pedidoRepository.save(p);
    }

    public void deleteById(Long id) {
        log.info("Eliminando pedido con id : {}", id);
        if(!pedidoRepository.existsById(id)) {
            throw new PedidoNoEncontradoException("Pedido no encontrado");
        }
        pedidoRepository.deleteById(id);
    }
}
