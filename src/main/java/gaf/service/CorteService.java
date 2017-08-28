package gaf.service;

import gaf.entity.Corte;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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

    public List<Corte> findAll() {
        return em.createQuery("from Corte").getResultList();
    }

    public Corte findById(Integer id) {
        return em.find(Corte.class, id);
    }

    public void update(Corte corte) {
        em.merge(corte);
    }
}
