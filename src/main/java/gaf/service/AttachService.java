package gaf.service;

import gaf.entity.Attach;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AttachService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void create(Attach attach) {
        log.info("[CREATE] " + attach);
        em.persist(attach);
    }

    public void update(Attach attach) {
        em.merge(attach);
    }

    public void delete(Attach attach) {
        log.info("[DELETE] " + attach);
        em.remove(em.contains(attach) ? attach : em.merge(attach));
    }

    // Search methods
    public List<Attach> findByCorteId(Integer corteId) {
        TypedQuery<Attach> query = em.createQuery("SELECT ATT FROM Attach ATT WHERE ATT.corteId = :corteId", Attach.class);
        query.setParameter("corteId", corteId);
        return query.getResultList();
    }
}
