package gaf.service;

import gaf.entity.Operador;
import org.primefaces.util.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OperadorService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void create(Operador operador) {
        log.info("[CREATE] " + operador);
        em.persist(operador);
    }

    public void delete(Operador operador) {
        log.info("[DELETE] " + operador);
        em.remove(em.contains(operador) ? operador : em.merge(operador));
    }

    public Operador findById(Integer id) {
        return em.find(Operador.class, id);
    }

    public void update(Operador operador) {
        em.merge(operador);
    }

    public List<Operador> findAll() {
        TypedQuery<Operador> query = em.createQuery("select o from Operador o", Operador.class);
        List<Operador> result = query.getResultList();
        if (result != null) {
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    public Operador findByUsername(String username) {
        TypedQuery<Operador> query = em.createQuery("select o from Operador o where o.username = :username", Operador.class);
        query.setParameter("username", username);
        List<Operador> result = query.getResultList();
        if (result != null && result.size() > 0) {
            return result.iterator().next();
        }
        return null;
    }
}
