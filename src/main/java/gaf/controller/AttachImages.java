package gaf.controller;

import gaf.entity.Attach;
import gaf.service.AttachService;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "attachImages")
public class AttachImages {

    @EJB private AttachService attachService;

    public List<StreamedContent> getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        List<StreamedContent> images = new ArrayList<>();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new ArrayList<>();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String corteId = context.getExternalContext().getRequestParameterMap().get("corteId");
            List<Attach> attachs = attachService.findByCorteId(Integer.valueOf(corteId));
            if (CollectionUtils.isNotEmpty(attachs)) {
                for (Attach attach : attachs) {
                    try {
                        String fileFullPath = attach.getPath() + attach.getFilename();
                        System.out.println(fileFullPath);
                        File file = new File(fileFullPath);
                        if (file.exists()) {
                            String contentType = new MimetypesFileTypeMap().getContentType(file);
                            System.out.println(contentType);
                            StreamedContent streamedContent = new DefaultStreamedContent(new FileInputStream(file), new MimetypesFileTypeMap().getContentType(file));
                            System.out.println("streamedContent=" + streamedContent);
                            images.add(streamedContent);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            //return new DefaultStreamedContent(new ByteArrayInputStream(student.getImage()));
        }
        return images;
    }
}
