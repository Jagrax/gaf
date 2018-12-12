package gaf.service;

import gaf.entity.Corte;
import gaf.util.Estados;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
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
        return em.createQuery("SELECT C FROM Corte C ORDER BY C.id DESC", Corte.class).getResultList();
    }

    public Corte findById(Integer id) {
        return em.find(Corte.class, id);
    }

    public List<Corte> findByStatusCerradoConDeuda() {
        List<Integer> estadosId = new ArrayList<>();
        estadosId.add(Estados.CORTE_CERRADO_CON_DEUDA.getId());
        return findByStatus(estadosId);
    }

    public List<Corte> findByStatus(List<Integer> listEstadosId) {
        TypedQuery<Corte> query = em.createQuery("SELECT c FROM Corte c WHERE c.estadoId IN :estadosId ORDER BY c.id DESC", Corte.class);
        query.setParameter("estadosId", listEstadosId);
        return query.getResultList();
    }

    public List<Corte> findAllNotFinished() {
        List<Integer> estadosId = new ArrayList<>();
        estadosId.add(Estados.CORTE_SIN_ASIGNAR.getId());
        estadosId.add(Estados.CORTE_EN_PRODUCCION.getId());
        estadosId.add(Estados.CORTE_CERRADO_CON_DEUDA.getId());
        return findByStatus(estadosId);
    }
}
