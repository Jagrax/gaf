package gaf.service;

import gaf.entity.CortesPagos;
import gaf.util.PersistenceHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class CortesPagosService {

    @Inject
    private Logger log;
    @Inject private EntityManager em;

    public void create(CortesPagos cortesPagos) {
        log.info("[CREATE] " + cortesPagos);
        em.persist(cortesPagos);
    }

    public void update(CortesPagos cortesPagos) {
        em.merge(cortesPagos);
    }

    public void delete(CortesPagos cortesPagos) {
        log.info("[DELETE] " + cortesPagos);
        em.remove(em.contains(cortesPagos) ? cortesPagos : em.merge(cortesPagos));
    }

    public CortesPagos findByCorteId(Integer id) {
        return (CortesPagos) PersistenceHelper.forceSingleResult(em.createQuery("SELECT P FROM CortesPagos P WHERE P.corteId = :id", CortesPagos.class).setParameter("id", id).getResultList());
    }
}
