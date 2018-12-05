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
@ManagedBean(name = "createOperador")
public class CreateOperadorController {

    private String username;
    private String description;
    private String password;

    @EJB private OperadorService operadorService;

    public void create() {
        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(password, salt);

        // Print out protected password
        System.out.println("My secure password = " + mySecurePassword);
        System.out.println("Salt value = " + salt);
        Operador op = new Operador();
        op.setUsername(username);
        op.setDescripcion(description);
        op.setSalt(salt);
        op.setPassword(mySecurePassword);
        operadorService.create(op);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
