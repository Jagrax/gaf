package gaf.controller;

import gaf.entity.Attachment;
import gaf.service.AttachService;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "attachmentsViewer")
public class AttachmentsViewerController {

    @EJB private AttachService attachService;

    private Integer id;

    public List<StreamedContent> getImages() throws IOException {
        List<StreamedContent> images = new ArrayList<>();
        if (id != null) {
            List<Attachment> attachments = attachService.findByCorteId(id);
            for (Attachment attachment : attachments) {
                File file = new File(attachment.getPath() + attachment.getFilename());
                images.add(new DefaultStreamedContent(new FileInputStream(file), new MimetypesFileTypeMap().getContentType(file)));
            }
        }
        return images;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
