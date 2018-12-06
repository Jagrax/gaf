package gaf.controller;

import gaf.entity.Access;
import gaf.entity.Operador;
import gaf.entity.Rol;
import gaf.service.AccessService;
import gaf.service.RolService;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ViewScoped
@ManagedBean(name = "accessController")
public class AccessController {

    private final static Logger log = Logger.getLogger(AccessController.class);

    @EJB private AccessService accessService;
    @EJB private RolService rolService;

    private Operador operador;
    private Rol operadorRol;

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public Operador getOperador() {
        if (operador == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            operador = (Operador) session.getAttribute("operador");
        }
        return operador;
    }

    public Rol getOperadorRol() {
        if (operadorRol == null) {
            Access access = accessService.findByOperatorId(operador.getId());
            if (access != null) {
                operadorRol = rolService.findById(access.getRolId());
            } else {
                log.warn("El " + operador + " no tiene definidos sus accesos");
            }
        }
        return operadorRol;
    }

    public boolean hasAccess(String accessRequirement) {
        if (getOperador() == null) return false;
        String operatorRolName = getOperadorRol().getName();

        // SU (Super User) has access to all
        if (operatorRolName.equals("SU")) {
            return true;
        }

        // ADMIN has access to all except SU access requirement
        if (operatorRolName.equals("ADMIN") && !accessRequirement.equals("SU")) {
            return true;
        }

        return operatorRolName.equals(accessRequirement);
    }

    public void checkAccessToPage(String accessRequirement) throws IOException {
        if (getOperador() != null) {
            if (!hasAccess(accessRequirement)) {
                Faces.redirect("access-denied.xhtml");
            }
        } else {
            Faces.redirect("login/login.xhtml");
        }
    }

    public boolean isOperario() {
        if (getOperadorRol() != null) {
            return operadorRol.getName().equals("OPERARIO");
        }
        return false;
    }
}
