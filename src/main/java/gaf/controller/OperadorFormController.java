package gaf.controller;

import gaf.entity.Operador;
import gaf.service.OperadorService;
import gaf.util.PasswordUtils;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;

import static gaf.util.Utils.addDetailMessage;

@ViewScoped
@ManagedBean(name = "operadorForm")
public class OperadorFormController {

    private Integer id;
    private Operador operador;

    @EJB
    private OperadorService operadorService;

    public void init() {
        if (Faces.isAjaxRequest()) {
            return;
        }
        if (id != null) {
            operador = operadorService.findById(id);
        } else {
            operador = new Operador();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public void remove() throws IOException {
        if (operador != null && operador.getId() != null) {
            operadorService.delete(operador);
            addDetailMessage("El operador " + operador.getUsername() + " se eliminó correctamente");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("operadorList.xhtml");
        }
    }

    public void save() throws IOException {
        String msg;
        if (operador.getId() == null) {
            String myPassword = operador.getPassword();

            // Generate Salt. The generated value can be stored in DB.
            String salt = PasswordUtils.getSalt(30);

            // Protect user's password. The generated value can be stored in DB.
            String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

            // Print out protected password
            System.out.println("My secure password = " + mySecurePassword);
            operador.setPassword(mySecurePassword);
            System.out.println("Salt value = " + salt);
            operador.setDescripcion(salt + operador.getDescripcion());
            operadorService.create(operador);
            msg = "El operador " + operador.getUsername() + " se creó correctamente";
        } else {
            String myPassword = operador.getPassword();

            // Generate Salt. The generated value can be stored in DB.
            String salt = PasswordUtils.getSalt(30);

            // Protect user's password. The generated value can be stored in DB.
            String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

            // Print out protected password
            System.out.println("My secure password = " + mySecurePassword);
            operador.setPassword(mySecurePassword);
            System.out.println("Salt value = " + salt);
            operador.setDescripcion(salt + operador.getDescripcion());
            operadorService.update(operador);
            msg = "El operador " + operador.getUsername() + " se actualizó correctamente";
        }
        addDetailMessage(msg);
        Faces.getFlash().setKeepMessages(true);
        Faces.redirect("operadorList.xhtml");
    }

    public void clear() {
        operador = new Operador();
        id = null;
    }

    public boolean isNew() {
        return operador == null || operador.getId() == null;
    }
}
