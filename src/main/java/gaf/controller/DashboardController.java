package gaf.controller;

import gaf.entity.Corte;
import gaf.service.CorteService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean(name = "dashboardController")
public class DashboardController {

    @EJB private CorteService corteService;

    private List<Corte> cortesInProcess;

    public List<Corte> getCortesInProcess() {
        if (cortesInProcess == null) {
            cortesInProcess = corteService.findByStatusNotFinished();
        }
        return cortesInProcess;
    }

    public void setCortesInProcess(List<Corte> cortesInProcess) {
        this.cortesInProcess = cortesInProcess;
    }
}
