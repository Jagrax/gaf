package gaf.service;

import gaf.entity.Talle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
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

    public List<Talle> findTallesFromCorte(Integer id) {
        return em.createQuery("from Talle where corte = " + id).getResultList();
    }

    public Talle findById(Integer id) {
        return em.find(Talle.class, id);
    }

    public void update(Talle talle) {
        em.merge(talle);
    }
}
