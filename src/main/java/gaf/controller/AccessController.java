package gaf.controller;

import gaf.entity.Operator;
import gaf.util.OperatorRolManager;
import org.omnifaces.util.Faces;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.IOException;

@ViewScoped
@ManagedBean(name = "accessController")
public class AccessController {

    @Inject private OperatorRolManager operatorRolManager;

    private Operator operator;

    public Operator getOperator() {
        return operatorRolManager.getOperatorFromSession(operator);
    }

    public boolean hasAccess(String accessRequirement) {
        return operatorRolManager.hasAccess(getOperator().getRol(), Operator.Rol.valueOf(accessRequirement));
    }

    public void checkAccessToPage(String accessRequirement) throws IOException {
        if (getOperator() != null) {
            if (!hasAccess(accessRequirement)) {
                Faces.redirect("access-denied.xhtml");
            }
        } else {
            Faces.redirect("login/login.xhtml");
        }
    }

    public boolean isWorker() {
        return getOperator().getRol().equals(Operator.Rol.WORKER);
    }
    public boolean isAdministrator() {
        return getOperator().getRol().equals(Operator.Rol.ADMINISTRATOR);
    }
}
