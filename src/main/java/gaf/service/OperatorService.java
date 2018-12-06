package gaf.service;

import gaf.entity.Operator;
import gaf.util.PersistenceHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OperatorService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Operator operator) {
        log.info("[CREATE] " + operator);
        em.persist(operator);
    }

    public void delete(Operator operator) {
        log.info("[DELETE] " + operator);
        em.remove(em.contains(operator) ? operator : em.merge(operator));
    }

    public Operator findById(Integer id) {
        return em.find(Operator.class, id);
    }

    public void update(Operator operator) {
        em.merge(operator);
    }

    public List<Operator> findAll() {
        return em.createQuery("SELECT O FROM Operator O", Operator.class).getResultList();
    }

    public Operator findByUsername(String username) {
        TypedQuery<Operator> query = em.createQuery("SELECT O FROM Operator O WHERE O.username = :username", Operator.class);
        query.setParameter("username", username);
        return (Operator) PersistenceHelper.forceSingleResult(query.getResultList());
    }
}
