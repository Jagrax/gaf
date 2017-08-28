package gaf.service;

import gaf.entity.Estado;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class EstadoService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public List<Estado> findAll() {
        List result = em.createQuery("from Estado").getResultList();
        log.info("Se encontraron " + result.size() + " estados.");
        return result;
    }

    public Estado find(Integer estadoId) {
        return em.find(Estado.class, estadoId);
    }
}
