package gaf.controller;

import gaf.entity.*;
import gaf.service.*;
import gaf.util.Estados;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@ViewScoped
@ManagedBean(name = "commonController")
public class CommonController {

    @EJB private RolService rolService;
    @EJB private AccessService accessService;
    @EJB private CorteService corteService;
    @EJB private TallerService tallerService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;

    private List<Estado> lstAllEstados;
    private List<Taller> lstAllTalleres;
    private List<Estado> lstTalleresEstados;
    private List<Estado> lstCortesAllEstados;
    private List<Integer> lstClothesSizes;

    private Operador operador;

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

        //TODO - Recuperar las DueDate de los cortes e insertalos en la lista para marcarlos en el calendario
        List<Corte> cortes = corteService.findAllNotFinished();
        if (CollectionUtils.isNotEmpty(cortes)) {
            for (Corte corte : cortes) {
                result.add(QUOTE + sdf.format(corte.getDueDate()) + TIME_FORMAT + QUOTE);
            }
        }

        return result.toArray(new String[cortes.size()]);
    }

    public boolean hasAccess(String accessKey) {
        if (operador == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            operador = (Operador) session.getAttribute("operador");
        }

        if (operador != null) {
            Access access = accessService.findByOperatorId(operador.getId());
            Rol rol = rolService.findById(access.getRolId());
            if (rol.getName().equals("SU")) {
                return true;
            }

            if (accessKey.contains("|")) {
                String[] keys = accessKey.split("[|]");
                for (String key : keys) {
                    if (rol.getName().equals(key)) return true;
                }
            }
        }

        return false;
    }
}
