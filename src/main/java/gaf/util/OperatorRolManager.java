package gaf.util;

import gaf.entity.Operator;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Stateless
public class OperatorRolManager {

    public Operator getOperatorFromSession(Operator op) {
        if (op == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            op = (Operator) session.getAttribute("operator");
        }
        return op;
    }

    public boolean hasAccess(Operator.Rol rol, Operator.Rol accessRequirement) {
        // SU (Super User) has access to all
        if (rol.equals(Operator.Rol.SU)) {
            return true;
        }

        // ADMIN has access to all except SU access requirement
        if (rol.equals(Operator.Rol.ADMINISTRATOR) && !accessRequirement.equals(Operator.Rol.SU)) {
            return true;
        }

        return rol.equals(accessRequirement);
    }
}
