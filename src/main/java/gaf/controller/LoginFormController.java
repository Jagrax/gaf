package gaf.controller;

import gaf.entity.Operador;
import gaf.service.OperadorService;
import gaf.util.PasswordUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ViewScoped
@ManagedBean(name = "loginForm")
public class LoginFormController {

    private static final Logger log = Logger.getLogger(LoginFormController.class);

    private String username;
    private String password;
    private String message = "";

    @EJB private OperadorService operadorService;

    public void login() throws IOException {
        Operador operador = operadorService.findByUsername(username);
        if (operador != null) {
            log.info("[LOGIN] Username: " + username + " Password: " + password);
            if (PasswordUtils.verifyUserPassword(password, operador.getPassword(), operador.getSalt())) {
                FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                session.setAttribute("operador", operador);
                Faces.redirect("common/dashboard/dashboard.jsf");
            } else {
                message = "La contraseña es incorrecta";
            }
        } else {
            message = "No existe el usuario " + username;
        }
    }

    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.removeAttribute("operador");
        Faces.redirect("login/login.xhtml");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
