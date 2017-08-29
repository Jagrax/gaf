package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Talle;
import gaf.service.CorteService;
import gaf.service.TalleService;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "corteForm")
public class CorteFormController {

    @EJB private CorteService corteService;
    @EJB private TalleService talleService;

    private Integer id;
    private Corte corte;
    private List<Talle> talles;
    private int cantTalles;

    @PostConstruct
    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            corte = corteService.findById(id);
            talles = talleService.findTallesFromCorte(id);
        } else {
            corte = new Corte();
            talles = new ArrayList<>();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    public List<Talle> getTalles() {
        return talles;
    }

    public void setTalles(List<Talle> talles) {
        this.talles = talles;
    }

    public int getCantTalles() {
        return cantTalles;
    }

    public void setCantTalles(int cantTalles) {
        this.cantTalles = cantTalles;
    }

    public void remove() throws IOException {
        if (corte != null && corte.getId() != null) {
            // Primero elimino los talles vinculados al corte
            for (Talle t: talles) {
                talleService.delete(t);
            }

            // Una vez eliminado los talles, recien ahora puedo eliminar el corte
            corteService.delete(corte);

            addDetailMessage("El corte " + corte.getName() + " se eliminó correctamente");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("corteList.xhtml");
        }
    }

    public void save() throws IOException {
        String msg;
        if (corte.getId() == null) {
            // Creo el corte
            corteService.create(corte);

            // Creo los talles para el corte
            for (Talle t : talles) {
                // Me aseguro de que el talle este vinculado al corte
                if (t.getCorteId() == null) t.setCorteId(corte.getId());

                talleService.create(t);
            }
            msg = "El corte " + corte.getName() + " se creó correctamente";
        } else {
            // Actualizo el corte
            corteService.update(corte);

            // Actualizo los talles del corte
            for (Talle t : talles) {
                talleService.update(t);
            }
            msg = "El corte " + corte.getName() + " se actualizó correctamente";
        }
        addDetailMessage(msg);
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("corteList.xhtml");
    }

    public void clear() {
        corte = new Corte();
        talles = new ArrayList<>();
        id = null;
        cantTalles = 0;
    }

    public boolean isNew() {
        return corte == null || corte.getId() == null;
    }

    public void generateTalles() throws IOException {
        calcularCantTalles();
        if (talles.size() == 0) {
            if (cantTalles != 0 && corte.getFromSize() == null && corte.getToSize() == null) {
                // Si no especifique desde/hasta que talle, voy a crea un solo talle (proyecto) que se le va a asignar a 1 taller
                Talle talle = new Talle();
                talle.setCorteId(corte.getId());
                talle.setSecondDueDate(corte.getDueDate());
                talle.setClothesDelivered(0);
                talle.setQuantity(corte.getClothesQuantity());
                talles.add(talle);
            } else {
                for (int n = 0; n < cantTalles; n++) {
                    Talle talle = new Talle();
                    talle.setCorteId(corte.getId());
                    talle.setSecondDueDate(corte.getDueDate());
                    talle.setClothesDelivered(0);
                    talle.setQuantity(Math.round(corte.getClothesQuantity() / cantTalles));
                    talles.add(talle);
                }
            }
        } else {
            save();
        }
    }

    private void calcularCantTalles() {
        Double fromSize = corte.getFromSize();
        Double toSize = corte.getToSize();
        if (fromSize != null && toSize != null) {
            if (toSize > fromSize) {
                cantTalles = 0;
                for (Double n = fromSize; n <= toSize; n = n + 2) {
                    cantTalles++;
                }
            } else {
                addDetailMessage("Los talles indicados no son validos. 'Desde el talle' es mas grande que 'Hasta el talle'");
            }
        } else if (cantTalles == 0) {
            addDetailMessage("Los talles indicados no son validos");
        }
    }
}
