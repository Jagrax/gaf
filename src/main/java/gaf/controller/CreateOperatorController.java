package gaf.controller;

import gaf.entity.Operator;
import gaf.service.OperatorService;
import gaf.util.PasswordUtils;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean(name = "createOperator")
public class CreateOperatorController {

    private String username;
    private String description;
    private String password;
    private Operator.Rol rol;
    private String message;

    @EJB private OperatorService operatorService;

    public void create() {
        // Generate Salt. The generated value can be stored in DB.
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(password, salt);

        // Print out protected password
        System.out.println("My secure password = " + mySecurePassword);
        System.out.println("Salt value = " + salt);
        Operator op = new Operator();
        op.setUsername(username);
        op.setDescription(description);
        op.setSalt(salt);
        op.setPassword(mySecurePassword);
        op.setRol(rol);
        operatorService.create(op);
        message = "Se creo al usuario " + username;
        username = "";
        description = "";
        password = "";
        rol = null;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Operator.Rol getRol() {
        return rol;
    }

    public void setRol(Operator.Rol rol) {
        this.rol = rol;
    }
}
