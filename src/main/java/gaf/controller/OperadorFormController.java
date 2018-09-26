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
        generatePassword();
        if (operador.getId() == null) {
            operadorService.create(operador);
            msg = "El operador " + operador.getUsername() + " se creó correctamente";
        } else {
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

    private void generatePassword() {
        String salt = PasswordUtils.getSalt(30);
        String mySecurePassword = PasswordUtils.generateSecurePassword(operador.getPassword(), salt);
        operador.setPassword(mySecurePassword);
        operador.setSalt(salt);
    }
}
