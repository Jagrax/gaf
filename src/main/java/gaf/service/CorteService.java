package gaf.service;

import gaf.entity.Corte;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CorteService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private TallerService tallerService;

    public void create(Corte corte) {
        Date creationDate = new Date();
        corte.setCreationDate(creationDate);

        log.info("[CREATE] " + corte);
        em.persist(corte);

        // Una vez que el corte fue dado de alta, tengo que poner al taller en estado 'En produccion'
        //tallerService.updateEstado(corte.getTallerId(), );
    }

    public void delete(Corte corte) {
        log.info("[DELETE] " + corte);
        em.remove(em.contains(corte) ? corte : em.merge(corte));
    }

    public List<Corte> findAll(String orderBy) {
        TypedQuery<Corte> query = em.createQuery("SELECT C FROM Corte C ORDER BY C.id " + orderBy, Corte.class);
        return query.getResultList();
    }

    public List<Corte> findAll() {
        return findAll("DESC");
    }

    public Corte findById(Integer id) {
        if (id != null) {
            return em.find(Corte.class, id);
        } else {
            return null;
        }
    }

    public void update(Corte corte) {
        em.merge(corte);
    }

    public List<Corte> findByStatusNotFinished() {
        TypedQuery<Corte> query = em.createQuery("SELECT c FROM Corte c WHERE c.estadoId IN (5, 6, 7)", Corte.class);
        return query.getResultList();
    }
}
