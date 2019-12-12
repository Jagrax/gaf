package gaf.controller;

import gaf.entity.*;
import gaf.service.AttachService;
import gaf.service.CorteService;
import gaf.service.TalleService;
import gaf.service.TallerService;
import gaf.util.Estados;
import gaf.util.FrontendValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "corteForm")
public class CorteFormController {

    private static final Logger log = Logger.getLogger(CorteFormController.class);

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private TallerService tallerService;

    private Integer id;
    private Corte corte;
    private List<Talle> talles;
    private List<Talle> tallesToRemove;
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
            List<Talle> tallesFromCorte = talleService.findTallesFromCorte(id);
            if (CollectionUtils.isNotEmpty(tallesFromCorte)) {
                Talle t = tallesFromCorte.iterator().next();
                firstDueDate = t.getFirstDueDate();
                secondDueDate = t.getSecondDueDate();
            }
        } else {
            corte = new Corte();
        }

        isMultiProyect = corte.getFromSize() != null && corte.getToSize() != null;
        tallesToRemove = new ArrayList<>();
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
        if (talles == null) {
            if (id != null) {
                talles = talleService.findTallesFromCorte(id);
            } else {
                talles = new ArrayList<>();
            }
        }
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

            addDetailMessage("corte.delete", null, corte.getName());
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
            msg = "corte.create";
        } else {
            log.info("Estoy editando el corte " + corte.getId());
            // Actualizo el corte
            corteService.update(corte);

            // Actualizo los talles del corte
            for (Talle t : talles) {
                if (t.getEstadoId().equals(Estados.CORTE_FINALIZADO.getId())) {
                    t.setFinalizationDate(new Date());
                }
                talleService.update(t);
            }

            for (Talle t : tallesToRemove) {
                talleService.delete(t);
            }
            msg = "corte.update";
        }
        updateTalleresStatus();
        addDetailMessage(msg, null, corte.getName());
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

    public void generateTalles() {
        calcularCantTalles();
        if (isValidaData()) {
            Taller gaf = tallerService.findByName("GAF");
            if (isMultiProyect) {
                // Si no tengo talles aun, los genero desde cero
                if (CollectionUtils.isEmpty(talles)) {
                    // Tengo que calcular la cantidad de proyectos a generar
                    for (Integer n = corte.getFromSize().intValue(); n <= corte.getToSize(); n += 2) {
                        Talle talle = new Talle();
                        talle.setQuantity(corte.getClothesQuantity() / cantTalles);
                        talle.setClothesDelivered(0);
                        talle.setSize(n.toString());
                        talle.setCorteId(corte.getId());
                        talle.setEstadoId(corte.getEstadoId());
                        talle.setFirstDueDate(firstDueDate);
                        talle.setSecondDueDate(secondDueDate);
                        if (corte.getEstadoId().equals(Estados.CORTE_EN_PRODUCCION.getId()) && gaf != null) talle.setTallerId(gaf.getId());
                        talles.add(talle);
                    }
                } else {
                    // Sino, tengo que ver cuantos tengo creados y calcular si tengo que crear nuevos o no
                    int diff = talles.size() - cantTalles;
                    // Si la diferencia es menor que cero, significa que tengo que generar los talles que me faltan (que serian una cantidad = a diff)
                    // Sino no hago nada
                    if (diff < 0) {
                        for (int n = 0; n < Math.abs(diff); n++) {
                            Talle talle = new Talle();
                            talle.setQuantity(null);
                            talle.setClothesDelivered(0);
                            talle.setSize(null);
                            talle.setCorteId(corte.getId());
                            talle.setEstadoId(corte.getEstadoId());
                            talle.setFirstDueDate(firstDueDate);
                            talle.setSecondDueDate(secondDueDate);
                            if (corte.getEstadoId().equals(Estados.CORTE_EN_PRODUCCION.getId()) && gaf != null) talle.setTallerId(gaf.getId());
                            talles.add(talle);
                        }
                    }
                }
            } else {
                Talle talle = new Talle();
                talle.setQuantity(cantTalles);
                talle.setClothesDelivered(0);
                talle.setCorteId(corte.getId());
                talle.setEstadoId(corte.getEstadoId());
                talle.setFirstDueDate(firstDueDate);
                talle.setSecondDueDate(secondDueDate);
                talles.add(talle);
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
                addDetailMessage("corte.talles.fromTalleIsBiggerThanToTalle");
            }
        } else if (cantTalles == 0) {
            addDetailMessage("corte.talles.invalidTalles");
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

        if (corte.getPrice() != null && !(Double.compare(corte.getPrice(), 0d) > 0)) {
            addDetailMessage("corte.error.price", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (corte.getClothesQuantity() != null && corte.getClothesQuantity() < 1) {
            addDetailMessage("corte.error.clothesQuantity", FacesMessage.SEVERITY_ERROR);
            isValidData = false;
        }

        if (!FrontendValidator.areEachDateAfter(firstDueDate, secondDueDate, corte.getDueDate())) {
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

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        if (uploadedFile != null && uploadedFile.getFileName() != null) {
            String fileUploadName = uploadedFile.getFileName();
            try {
                String attachPath = "C:/wildfly-14.0.1.Final/standalone/deployments/gaf/attachs/";
                String fileUploadWithPath = FilenameUtils.concat(attachPath, fileUploadName);

                log.info("Subiend el archivo [" + fileUploadName + "] a [" + fileUploadWithPath + "]");
                InputStream inputStream = uploadedFile.getInputstream();
                FileOutputStream outputStream = new FileOutputStream(fileUploadWithPath);
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();

                Attachment attachment = new Attachment();
                attachment.setPath(attachPath);
                attachment.setFilename(fileUploadName);
                attachment.setCorteId(id);
                attachService.create(attachment);
            } catch (IOException e) {
                String message = "Error al procesar la carga del archivo [" + fileUploadName + "]";
                addDetailMessage(message, FacesMessage.SEVERITY_FATAL);
                log.error(message, e);
            }

            FacesMessage message = new FacesMessage("Listo!", "El archivo " + fileUploadName + " se cargo correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void checkTalleStatus(Talle talle) {
        if (talle.getTallerId() != null && talle.getEstadoId().equals(Estados.CORTE_SIN_ASIGNAR.getId())) {
            talle.setEstadoId(Estados.CORTE_EN_PRODUCCION.getId());
        }
    }

    public void deleteTalle(Talle talle) {
        tallesToRemove.add(talle);
        talles.remove(talle);
    }
}
