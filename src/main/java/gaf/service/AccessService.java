package gaf.service;

import gaf.entity.Access;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AccessService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Access access) {
        log.info("[CREATE] " + access);
        em.persist(access);
    }

    public void update(Access access) {
        em.merge(access);
    }

    public void delete(Access access) {
        log.info("[DELETE] " + access);
        em.remove(em.contains(access) ? access : em.merge(access));
    }

    public Access findByOperatorId(Integer operatorId) {
        TypedQuery<Access> query = em.createQuery("SELECT A FROM Access A WHERE A.operadorId = :operadorId", Access.class);
        query.setParameter("operadorId", operatorId);
        return query.getSingleResult();
    }
}
