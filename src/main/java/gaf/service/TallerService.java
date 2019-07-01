package gaf.service;

import gaf.entity.Taller;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TallerService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Taller taller) {
        log.info("[CREATE] " + taller);
        em.persist(taller);
    }

    public void update(Taller taller) {
        em.merge(taller);
    }

    public void delete(Taller taller) {
        log.info("[DELETE] " + taller);
        em.remove(em.contains(taller) ? taller : em.merge(taller));
    }

    public List<Taller> findAll() {
        return em.createQuery("SELECT T FROM Taller T", Taller.class).getResultList();
    }

    public Taller findById(Integer id) {
        return em.find(Taller.class, id);
    }

    public Taller findByName(String name) {
        TypedQuery<Taller> query = em.createQuery("SELECT t FROM Taller t WHERE t.name = :name", Taller.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
