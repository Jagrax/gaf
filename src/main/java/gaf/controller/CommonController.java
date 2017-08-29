package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Estado;
import gaf.entity.Talle;
import gaf.entity.Taller;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import gaf.service.TallerService;
import gaf.util.Estados;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "commonController")
public class CommonController {

    @EJB private CorteService corteService;
    @EJB private TallerService tallerService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;
    private List<Estado> lstAllEstados;
    private List<Estado> lstTalleresEstados;
    private List<Estado> lstCortesAllEstados;
    private List<Integer> lstClothesSizes;

    @PostConstruct
    public void init() {
        lstAllEstados = new ArrayList<>();
        lstTalleresEstados = new ArrayList<>();
        lstCortesAllEstados = new ArrayList<>();
        lstClothesSizes = new ArrayList<>();

        for (Estados estados : Estados.values()) {
            Estado e = new Estado();
            e.setId(estados.getId());
            e.setName(estados.getName());
            e.setColor(estados.getColor());
            e.setEntity(estados.getEntity());
            switch (e.getEntity()) {
                case "TALLER": {
                    lstTalleresEstados.add(e);
                    break;
                }
                case "CORTE": {
                    lstCortesAllEstados.add(e);
                    break;
                }
                default: {
                    System.err.println("La entity '" + e.getEntity() + "' no es valida.");
                }
            }
            lstAllEstados.add(e);
        }

        for (int n = 0; n <= 100; n++) {
            lstClothesSizes.add(n);
        }
    }

    /**
     * Devuelve un object Estado correspondiente al id
     */
    public Estado getEstado(Integer id) {
        return estadoService.find(id);
    }

    /**
     * Devuelve los estilos para generar una caja con los colores
     * correspondientes al estado del taller/corte
     */
    public String getStyleClassForEstado(Integer idEstado) {
        Estado e = estadoService.find(idEstado);
        if (e != null) {
            String styleClass;
            switch (e.getColor()) {
                case "blue": {
                    styleClass = "bg-primary text-white text-center";
                    break;
                }
                case "green": {
                    styleClass = "bg-success text-white text-center";
                    break;
                }
                case "yellow": {
                    styleClass = "bg-yellow text-center";
                    break;
                }
                case "orange": {
                    styleClass = "bg-warning text-white text-center";
                    break;
                }
                case "red": {
                    styleClass = "bg-danger text-white text-center";
                    break;
                }
                default: {
                    styleClass = "text-center";
                }
            }
            return styleClass;
        }
        return "";
    }

    public List<Estado> getLstAllEstados() {
        return lstAllEstados;
    }

    public List<Estado> getLstTalleresEstados() {
        return lstTalleresEstados;
    }

    public List<Estado> getLstCortesAllEstados() {
        return lstCortesAllEstados;
    }

    public List<Integer> getLstClothesSizes() {
        return lstClothesSizes;
    }

    public Corte getCorteById(Integer id) {return corteService.findById(id);}
    public Taller getTallerById(Integer id) {return tallerService.findById(id);}
    public Talle getTalleById(Integer id) {return talleService.findById(id);}
}
