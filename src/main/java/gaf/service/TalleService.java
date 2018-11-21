package gaf.service;

import gaf.entity.Talle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TalleService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Talle talle) {
        log.info("[CREATE] " + talle);
        em.persist(talle);
    }

    public void update(Talle talle) {
        em.merge(talle);
    }

    public void delete(Talle talle) {
        log.info("[DELETE] " + talle);
        em.remove(em.contains(talle) ? talle : em.merge(talle));
    }

    @SuppressWarnings(value = "unused")
    public List<Talle> findAll() {
        return em.createQuery("SELECT T FROM Talle T", Talle.class).getResultList();
    }

    public Talle findById(Integer id) {
        return em.find(Talle.class, id);
    }

    public List<Talle> findTallesFromCorte(Integer id) {
        TypedQuery<Talle> query = em.createQuery("SELECT T FROM Talle T WHERE T.corteId = :corteId", Talle.class);
        query.setParameter("corteId", id);
        return query.getResultList();
    }
}
