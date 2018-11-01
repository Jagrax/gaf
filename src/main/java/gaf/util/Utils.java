package gaf.util;

import org.omnifaces.util.Messages;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

@ApplicationScoped
public class Utils implements Serializable {

    public static void addDetailMessage(String message) {
        addDetailMessage(message, null);
    }

    public static void addDetailMessage(String message, FacesMessage.Severity severity, Object... params) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle resource = ResourceBundle.getBundle("resources", context.getViewRoot().getLocale());
        if (params == null) {
            message = resource.getString(message);
        } else {
            message = MessageFormat.format(resource.getString(message), params);
        }
        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
        Messages.add(null,facesMessage);
    }
}
