package gaf.service;

import gaf.entity.Corte;
import gaf.util.Estados;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CorteService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Corte corte) {
        corte.setCreationDate(new Date());

        log.info("[CREATE] " + corte);
        em.persist(corte);
    }

    public void update(Corte corte) {
        em.merge(corte);
    }

    public void delete(Corte corte) {
        log.info("[DELETE] " + corte);
        em.remove(em.contains(corte) ? corte : em.merge(corte));
    }

    public List<Corte> findAll() {
        return findAll("DESC");
    }

    public List<Corte> findAll(String orderBy) {
        return em.createQuery("SELECT C FROM Corte C ORDER BY C.id " + orderBy, Corte.class).getResultList();
    }

    public Corte findById(Integer id) {
        return em.find(Corte.class, id);
    }

    public List<Corte> findByStatusNotFinished() {
        TypedQuery<Corte> query = em.createQuery("SELECT c FROM Corte c WHERE c.estadoId IN (:sinAsignar, :enProduccion, :cerradoConDeuda)", Corte.class);
        query.setParameter("sinAsignar", Estados.CORTE_SIN_ASIGNAR.getId());
        query.setParameter("enProduccion", Estados.CORTE_EN_PRODUCCION.getId());
        query.setParameter("cerradoConDeuda", Estados.CORTE_CERRADO_CON_DEUDA.getId());
        return query.getResultList();
    }

    public List<Corte> findByStatusCerradoConDeuda() {
        TypedQuery<Corte> query = em.createQuery("SELECT c FROM Corte c WHERE c.estadoId = :estadoId", Corte.class);
        query.setParameter("estadoId", Estados.CORTE_CERRADO_CON_DEUDA.getId());
        return query.getResultList();
    }
}
