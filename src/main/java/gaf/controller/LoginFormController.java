package gaf.controller;

import gaf.entity.Operador;
import gaf.service.OperadorService;
import gaf.util.PasswordUtils;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ViewScoped
@ManagedBean(name = "loginForm")
public class LoginFormController {

    private String username;
    private String password;

    @EJB
    private OperadorService operadorService;

    public void login() {
        System.out.println("Por hacer login!");
        Operador operador = operadorService.findByUsername(username);
        if (operador != null) {
            if (PasswordUtils.verifyUserPassword(password, operador.getPassword(), operador.getSalt())) {
                System.out.println("Provided user password " + password + " is correct.");
                FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                session.setAttribute("operador", operador);
            } else {
                System.out.println("La contraseña es incorrecta");
            }
        } else {
            System.out.println("No existe el usuario " + username);
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
}
