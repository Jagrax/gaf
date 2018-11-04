package gaf.controller;

import gaf.entity.Corte;
import gaf.service.CorteService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean(name = "reposicionesController")
public class ReposicionesController {

    @EJB private CorteService corteService;

    private List<Corte> cortesCerradosConDeuda;

    public List<Corte> getCortesCerradosConDeuda() {
        if (cortesCerradosConDeuda == null) {
            cortesCerradosConDeuda = corteService.findByStatusCerradoConDeuda();
        }
        return cortesCerradosConDeuda;
    }

    public void setCortesCerradosConDeuda(List<Corte> cortesCerradosConDeuda) {
        this.cortesCerradosConDeuda = cortesCerradosConDeuda;
    }
}
