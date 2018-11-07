package gaf.service;

import gaf.entity.Estado;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EstadoService {

    @Inject private EntityManager em;

    public Estado findById(Integer estadoId) {
        return em.find(Estado.class, estadoId);
    }

    @SuppressWarnings(value = "unused")
    public List<Estado> findAll() {
        return em.createQuery("SELECT E FROM Estado E", Estado.class).getResultList();
    }
}
