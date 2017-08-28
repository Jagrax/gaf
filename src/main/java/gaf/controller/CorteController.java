package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Taller;
import gaf.service.CorteService;
import gaf.service.TallerService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteController")
public class CorteController {



    private List<Corte> lstCortes;
    private Corte corte;
    @EJB private CorteService corteService;
    private List<Taller> lstTalleresDisponibles;
    @EJB private TallerService tallerService;

    @PostConstruct
    public void init() {
        lstCortes = corteService.findAll();
        lstTalleresDisponibles = tallerService.findTalleresDisponibles();
        corte = new Corte();
        corte.setFromSize(0.0);
        corte.setToSize(100.0);


    }

    public List<Corte> getLstCortes() {
        return lstCortes;
    }

    public List<Taller> getLstTalleresDisponibles() {
        return lstTalleresDisponibles;
    }

    public Corte getCorte() {
        return corte;
    }

    // CRUD Methods
    public void create() {
        corteService.create(corte);
        lstCortes.add(corte);
        corte = new Corte();
    }

    public void delete(Corte corte) {
        corteService.delete(corte);
        lstCortes.remove(corte);
    }

/*
    public void create() throws Exception {
        try {
            corteService.create(newCorte);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
        } catch (Exception e) {
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
*/
}
