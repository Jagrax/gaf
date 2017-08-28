package gaf.service;

import gaf.entity.Taller;
import gaf.util.Estados;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TallerService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void create(Taller taller) {
        log.info("[CREATE] " + taller);
        em.persist(taller);
    }

    public void delete(Taller taller) {
        log.info("[DELETE] " + taller);
        em.remove(em.contains(taller) ? taller : em.merge(taller));
    }

    public void update(Taller taller) {
        em.merge(taller);
    }

    public List<Taller> findTalleresDisponibles() {
        List<Taller> result;
        try {
            result = em.createQuery("from Taller where estado = " + Estados.TALLER_DISPONIBLE.getId()).getResultList();
        } catch (Exception e) {
            log.info("Se produjo un error al buscar los talleres disponibles." + e.getMessage());
            result = new ArrayList<>();
        }
        return result;
    }

    public void updateEstado(Integer tallerId, Integer estadoId) {
        Taller taller = em.find(Taller.class, tallerId);
        taller.setEstadoId(estadoId);
        em.merge(taller);
    }

    public Taller findById(Integer id) {
        return em.find(Taller.class, id);
    }

    public List<Taller> findAll() {
        List<Taller> result;
        try {
            result = em.createQuery("from Taller").getResultList();
        } catch (Exception e) {
            log.info("Se produjo un error al buscar todos los talleres. " + e.getMessage());
            result = new ArrayList<>();
        }
        return result;
    }
}
