package gaf.service;

import gaf.entity.Talle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class TalleService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void create(Talle talle) {
        log.info("[CREATE] " + talle);
        em.persist(talle);
    }

    public void delete(Talle talle) {
        log.info("[DELETE] " + talle);
        em.remove(em.contains(talle) ? talle : em.merge(talle));
    }
}
