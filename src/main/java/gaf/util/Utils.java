package gaf.util;

import org.omnifaces.util.Messages;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import java.io.Serializable;

@ApplicationScoped
public class Utils implements Serializable {

    public static void addDetailMessage(String message){
        addDetailMessage(message, null);
    }

    public static void addDetailMessage(String message, FacesMessage.Severity severity){

        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if(severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        } else{
            Messages.add(null,facesMessage);
        }
    }

    public static boolean isEmpty(String string) {
        return (string == null || string.trim().equals(""));
    }
}
