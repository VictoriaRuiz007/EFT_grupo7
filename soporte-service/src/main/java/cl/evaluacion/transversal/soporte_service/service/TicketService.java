package cl.evaluacion.transversal.soporte_service.service;

import cl.evaluacion.transversal.soporte_service.dto.TicketRequest;
import cl.evaluacion.transversal.soporte_service.exception.EstadoNoEncontradoException;
import cl.evaluacion.transversal.soporte_service.exception.TicketNoEncontradoException;
import cl.evaluacion.transversal.soporte_service.model.Ticket;
import cl.evaluacion.transversal.soporte_service.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> findAll(){
        log.info("Listando tickets de soporte");
        return ticketRepository.findAll();
    }

    public Ticket findById(Long id){
        log.info("Buscando ticket con id : {}", id);
        return ticketRepository.findById(id)
                .orElseThrow(()-> new TicketNoEncontradoException("Ticket no encontrado"));
    }

    public List<Ticket> findByEstado(String estado){
        log.info("Listando tickets con estado : {}", estado);
        List<Ticket> ts = ticketRepository.findByEstado(estado);

        if(ts.isEmpty()){
            throw new EstadoNoEncontradoException("Estado no encontrado");
        }
        return ts;
    }

    public Ticket save(TicketRequest tr){
        log.info("Creando nuevo ticket: {}", tr);

        Ticket t = new Ticket();
        t.setAsunto(tr.getAsunto());
        t.setEstado(tr.getEstado());
        t.setDescripcion(tr.getDescripcion());
        t.setIdUsuario(tr.getIdUsuario());
        t.setFechaCreacion(tr.getFechaCreacion());
        return ticketRepository.save(t);
    }

    public void deleteById(Long id){
        log.info("Eliminando ticket con id : {}", id);
        if(!ticketRepository.existsById(id)){
            throw new TicketNoEncontradoException("Ticket no encontrado");
        }
        ticketRepository.deleteById(id);
    }
}
