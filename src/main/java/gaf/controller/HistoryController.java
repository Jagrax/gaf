package gaf.controller;

import gaf.entity.Corte;
import gaf.service.CorteService;
import gaf.util.Estados;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "historyController")
public class HistoryController {

    @EJB
    private CorteService corteService;

    private List<Corte> cortesFinalizados;

    public List<Corte> getCortesFinalizados() {
        if (cortesFinalizados == null) {
            cortesFinalizados = corteService.findByStatus(Collections.singletonList(Estados.CORTE_FINALIZADO.getId()));
        }
        return cortesFinalizados;
    }

    public void setCortesFinalizados(List<Corte> cortesFinalizados) {
        this.cortesFinalizados = cortesFinalizados;
    }

    public void markCorteAsInProcess(Corte corte) {
        corte.setEstadoId(Estados.CORTE_EN_PRODUCCION.getId());
        corteService.update(corte);
        addDetailMessage("corte.update.historyToCorte", null, corte.getName());
        cortesFinalizados = null;
    }
}
