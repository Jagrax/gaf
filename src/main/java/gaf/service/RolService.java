package gaf.service;

import gaf.entity.Rol;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

@Stateless
public class RolService {

    @Inject private Logger log;
    @Inject private EntityManager em;

    public void create(Rol rol) {
        log.info("[CREATE] " + rol);
        em.persist(rol);
    }

    public void update(Rol rol) {
        em.merge(rol);
    }

    public void delete(Rol rol) {
        log.info("[DELETE] " + rol);
        em.remove(em.contains(rol) ? rol : em.merge(rol));
    }

    public Rol findById(Integer id) {
        return em.find(Rol.class, id);
    }
}
