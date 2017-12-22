package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Estado;
import gaf.entity.Talle;
import gaf.entity.Taller;
import gaf.service.CorteService;
import gaf.service.TalleService;
import gaf.service.TallerService;
import gaf.util.Estados;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "corteForm")
public class CorteFormController {

    private static final Logger log = Logger.getLogger(CorteFormController.class);

    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private TallerService tallerService;

    private Integer id;
    private Corte corte;
    private List<Talle> talles;
    private int cantTalles;

    private Date firstDueDate;
    private Date secondDueDate;

    private boolean isMultiProyect;

    @PostConstruct
    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            corte = corteService.findById(id);
            talles = talleService.findTallesFromCorte(id);
            Talle t = talleService.findTallesFromCorte(id).iterator().next();
            firstDueDate = t.getFirstDueDate();
            secondDueDate = t.getSecondDueDate();
        } else {
            corte = new Corte();
            talles = new ArrayList<>();
        }
        if (corte.getFromSize() != null && corte.getToSize() != null) {
            isMultiProyect = true;
        } else {
            isMultiProyect = false;
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

    public boolean isMultiProyect() {
        return isMultiProyect;
    }

    public void setMultiProyect(boolean multiProyect) {
        isMultiProyect = multiProyect;
    }

    public Date getFirstDueDate() {
        return firstDueDate;
    }

    public void setFirstDueDate(Date firstDueDate) {
        this.firstDueDate = firstDueDate;
    }

    public Date getSecondDueDate() {
        return secondDueDate;
    }

    public void setSecondDueDate(Date secondDueDate) {
        this.secondDueDate = secondDueDate;
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
            log.info("Voy a crear un corte nuevo");
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
            log.info("Estoy editando el corte " + corte.getId());
            // Actualizo el corte
            corteService.update(corte);

            // Actualizo los talles del corte
            for (Talle t : talles) {
                talleService.update(t);
            }
            msg = "El corte " + corte.getName() + " se actualizó correctamente";
        }
        updateTalleresStatus();
        addDetailMessage(msg);
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("corteList.xhtml");
    }

    private void updateTalleresStatus() {
        for (Talle talle : talles) {
            if (talle.getEstadoId().equals(Estados.CORTE_EN_PRODUCCION.getId())) {
                Taller taller = tallerService.findById(talle.getTallerId());
                if (!taller.getEstadoId().equals(Estados.TALLER_EN_PRODUCCION.getId())) {
                    taller.setEstadoId(Estados.TALLER_EN_PRODUCCION.getId());
                    tallerService.update(taller);
                }
            }
        }
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
        if (isValidaData()) {
            if (talles.size() == 0) {
                if (isMultiProyect) {
                    // Tengo que calcular la cantidad de proyectos a generar
                    for (Integer n = corte.getFromSize().intValue(); n <= corte.getToSize(); n += 2) {
                        Talle talle = new Talle();
                        talle.setQuantity(corte.getClothesQuantity() / cantTalles);
                        talle.setClothesDelivered(0);
                        talle.setSize(n.toString());
                        talle.setCorteId(corte.getId());
                        talle.setEstadoId(Estados.CORTE_EN_PRODUCCION.getId());
                        talle.setFirstDueDate(firstDueDate);
                        talle.setSecondDueDate(secondDueDate);
                        talles.add(talle);
                    }
                } else {
                    Talle talle = new Talle();
                    talle.setQuantity(cantTalles);
                    talle.setClothesDelivered(0);
                    talle.setCorteId(corte.getId());
                    talle.setEstadoId(Estados.CORTE_EN_PRODUCCION.getId());
                    talle.setFirstDueDate(firstDueDate);
                    talle.setSecondDueDate(secondDueDate);
                    talles.add(talle);
                }
            } else {
                save();
            }
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

    public String getBtnLabel() {
        if (talles == null || talles.size() == 0) {
            return "Generar talles";
        } else {
            return "Guardar";
        }
    }

    private boolean isValidaData() {
        boolean isValidData = true;

        // VALIDACION DE CORTES
        if (StringUtils.isEmpty(corte.getName())) {
            addDetailMessage("corte.error.name", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (corte.getClothesQuantity() == null || corte.getClothesQuantity() < 1) {
            addDetailMessage("corte.error.clothesQuantity", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (corte.getDueDate() == null || corte.getDueDate().before(new Date())) {
            addDetailMessage("corte.error.dueDate", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        boolean isValidEstado = false;
        for (Estados e : Estados.values()) {
            if (Objects.equals(corte.getEstadoId(), e.getId())) isValidEstado = true;
        }
        if (!isValidEstado) {
            addDetailMessage("corte.error.estado", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (cantTalles < 1) {
            addDetailMessage("corte.error.minTalles", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (isMultiProyect) {
            if (corte.getFromSize() == null || corte.getFromSize() < 1) {
                addDetailMessage("corte.error.fromSizeInvalid", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }
            if (corte.getToSize() == null || corte.getToSize() < 1) {
                addDetailMessage("corte.error.toSizeInvalid", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }
        }

        // VALIDACION DE PROYECTOS/TALLES
        for (Talle talle : talles) {
            isValidEstado = false;
            for (Estados e : Estados.values()) {
                if (Objects.equals(talle.getEstadoId(), e.getId())) isValidEstado = true;
            }
            if (!isValidEstado) {
                addDetailMessage("talle.error.estado", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }

            if (talle.getFirstDueDate() == null || talle.getFirstDueDate().before(new Date())) {
                addDetailMessage("talle.error.firstDueDate", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }

            if (talle.getSecondDueDate() == null || talle.getSecondDueDate().before(new Date())) {
                addDetailMessage("talle.error.secondDueDate", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }

            if (talle.getEstadoId().equals(Estados.CORTE_EN_PRODUCCION.getId()) && talle.getTallerId() == null) {
                addDetailMessage("talle.error.tallerNotSelected", FacesMessage.SEVERITY_ERROR);
                isValidData = false;
            }
        }

        return isValidData;
    }
}
