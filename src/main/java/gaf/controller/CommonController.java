package gaf.controller;

import gaf.entity.*;
import gaf.manager.SecurityManager;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import gaf.service.TallerService;
import gaf.util.Estados;
import gaf.util.OperatorRolManager;
import org.apache.commons.collections4.CollectionUtils;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "commonController")
public class CommonController {

    @EJB private CorteService corteService;
    @EJB private TallerService tallerService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;
    @Inject private SecurityManager securityManager;
    @Inject private OperatorRolManager operatorRolManager;

    private List<Estado> lstAllEstados;
    private List<Taller> lstAllTalleres;
    private List<Estado> lstTalleresEstados;
    private List<Estado> lstCortesAllEstados;
    private List<Integer> lstClothesSizes;

    private Operator operator;

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

    public Operator getOperator() {
        return operatorRolManager.getOperatorFromSession(operator);
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
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

    public List<Taller> getLstAllTalleres() {
        if (lstAllTalleres == null) lstAllTalleres = tallerService.findAll();
        return lstAllTalleres;
    }

    public void setLstAllTalleres(List<Taller> lstAllTalleres) {
        this.lstAllTalleres = lstAllTalleres;
    }

    // ----- GETTERS DE ENTITDADES POR ID -----

    public Corte getCorteById(Integer id) {return corteService.findById(id);}
    public Estado getEstadoById(Integer id) {
        return estadoService.findById(id);
    }
    public Taller getTallerById(Integer id) {return tallerService.findById(id);}
    public Talle getTalleById(Integer id) {return talleService.findById(id);}

    public void showCalendar() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.execute("PF('gaf-calendar-dialog').show()");
    }

    private static final String QUOTE = "'";
    private static final String TIME_FORMAT = "T00:00:00";
    private static final String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";

    /* SE LLAMA DESDE EL JS EN default.xhtml:73 */
    public String[] getFechasVencimientos() {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT);
        List<String> result = new ArrayList<>();

        List<Corte> cortes = corteService.findAllNotFinished();
        if (CollectionUtils.isNotEmpty(cortes)) {
            for (Corte corte : cortes) {
                if (corte.getDueDate() != null) {
                    result.add(QUOTE + sdf.format(corte.getDueDate()) + TIME_FORMAT + QUOTE);
                }
            }
        }

        return result.toArray(new String[cortes.size()]);
    }

    public StreamedContent getProfileImage(String contextPath) throws FileNotFoundException {
        StreamedContent img = null;
        String baseDirectory = contextPath + "/resources/gfx/";
        if (operator == null) {
            operator = operatorRolManager.getOperatorFromSession(operator);
        }

        if (operator != null) {
            img = new DefaultStreamedContent(new FileInputStream(baseDirectory + operator.getRol().name() + ".png"), new MimetypesFileTypeMap().getContentType(baseDirectory + operator.getRol().name() + ".png"));
        }

        return img;
    }

    public void logout() throws IOException {
        securityManager.logout();
        Faces.redirect("login/login.xhtml");
    }
}
