package gaf.controller;

import gaf.entity.Operator;
import gaf.manager.SecurityManager;
import gaf.service.OperatorService;
import gaf.util.PasswordUtils;
import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ViewScoped
@ManagedBean(name = "loginForm")
public class LoginFormController {

    private static final Logger log = Logger.getLogger(LoginFormController.class);

    private String username;
    private String password;
    private String message = "";

    @EJB private OperatorService operatorService;
    @Inject private SecurityManager securityManager;

    public void login() throws IOException {
        Operator operator = operatorService.findByUsername(username);
        if (operator != null) {
            if (securityManager.login(operator, password)) {
                Faces.redirect("corteList.jsf");
            } else {
                message = "La contraseña es incorrecta";
            }
        } else {
            message = "No existe el usuario " + username;
        }
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
