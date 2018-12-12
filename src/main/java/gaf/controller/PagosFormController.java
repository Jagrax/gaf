package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.CortesPagos;
import gaf.entity.FrontEndCorte;
import gaf.entity.Talle;
import gaf.service.CorteService;
import gaf.service.CortesPagosService;
import gaf.service.TalleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "pagosForm")
public class PagosFormController {

    private static final Logger log = Logger.getLogger(PagosFormController.class);

    @Inject private CorteService corteService;
    @Inject private TalleService talleService;
    @Inject private CortesPagosService cortesPagosService;

    private Integer id;
    private Corte corte;
    private CortesPagos cortesPagos;
    private List<Talle> talles;

    @PostConstruct
    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Corte getCorte() {
        if (corte == null) {
            if (id != null) {
                corte = corteService.findById(id);
            } else {
                corte = new Corte();
            }
        }
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    public CortesPagos getCortesPagos() {
        if (cortesPagos == null) {
            cortesPagos = cortesPagosService.findByCorteId(id);
            if (cortesPagos == null) {
                cortesPagos = new CortesPagos();
                cortesPagos.setCorteId(id);
            }
        }
        return cortesPagos;
    }

    public void setCortesPagos(CortesPagos cortesPagos) {
        this.cortesPagos = cortesPagos;
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

    public void save() throws IOException {
        String msg;
        if (cortesPagos.getId() == null) {
            log.info("Voy a crear un nuevo pago para el corte " + id);
            cortesPagosService.create(cortesPagos);
            msg = "pagos.create";
        } else {
            log.info("Estoy editando los pagos del corte " + corte.getId());
            // Actualizo el corte
            cortesPagosService.update(cortesPagos);
            msg = "pagos.update";
        }

        // Actualizo los talles del corte
        for (Talle t : talles) {
            talleService.update(t);
        }

        addDetailMessage(msg, null, corte.getName());
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("common/pagos/pagosList.xhtml?authorized=true");

    }
}
