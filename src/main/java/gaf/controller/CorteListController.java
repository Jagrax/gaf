package gaf.controller;

import gaf.entity.Corte;
import gaf.service.CorteService;
import gaf.util.Utils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteList")
public class CorteListController {

    private List<Corte> cortes;

    @EJB
    private CorteService corteService;

    @PostConstruct
    public void init() {
        cortes = corteService.findAll();
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
    }

    public String generateCorteLabelForPanel(Corte corte) {
        String label = corte.getName();
        if (corte.getPrice() != null) label += "|$" + corte.getPrice();
        label += "|Cant. de prendas: " + corte.getClothesQuantity();
        return label;
    }
}
