package gaf.service;

import gaf.entity.Attachment;

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

    public void create(Attachment attachment) {
        log.info("[CREATE] " + attachment);
        em.persist(attachment);
    }

    public void update(Attachment attachment) {
        em.merge(attachment);
    }

    public void delete(Attachment attachment) {
        log.info("[DELETE] " + attachment);
        em.remove(em.contains(attachment) ? attachment : em.merge(attachment));
    }

    // Search methods
    public List<Attachment> findByCorteId(Integer corteId) {
        TypedQuery<Attachment> query = em.createQuery("SELECT ATT FROM Attachment ATT WHERE ATT.corteId = :corteId", Attachment.class);
        query.setParameter("corteId", corteId);
        return query.getResultList();
    }
}
