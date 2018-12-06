package gaf.manager;

import gaf.entity.Operator;
import gaf.util.PasswordUtils;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@Stateless
public class SecurityManager {

    private final static Logger log = Logger.getLogger(SecurityManager.class);

    public boolean login(Operator operator, String password) {
        if (PasswordUtils.verifyUserPassword(password, operator.getPassword(), operator.getSalt())) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.setAttribute("operator", operator);
            log.info("[LOGIN] " + operator);
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Operator operator = (Operator) session.getAttribute("operator");
        if (operator != null) {
            session.removeAttribute("operator");
            log.info("[LOGOUT] " + operator);
        }
    }
}
