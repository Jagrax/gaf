package gaf.controller;

import com.groupdocs.viewer.config.ViewerConfig;
import com.groupdocs.viewer.converter.options.HtmlOptions;
import com.groupdocs.viewer.domain.html.PageHtml;
import com.groupdocs.viewer.handler.ViewerHtmlHandler;
import gaf.entity.Attachment;
import gaf.service.AttachService;
import gaf.util.Utilities;
import org.apache.commons.lang3.StringUtils;
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

    public String getAttachmentPage(Integer id) {
        String url = null;
        if (id != null) {
            List<Attachment> attachments = attachService.findByCorteId(id);
            String fileName = attachments.iterator().next().getFilename();
            if (StringUtils.isNotEmpty(fileName)) url = createHtmlFile(fileName);
        }
        return url;
    }

    private String createHtmlFile(String fileName) {
        try {
            // Setup GroupDocs.Viewer config
            ViewerConfig config = Utilities.getConfiguration();

            // Create html handler
            ViewerHtmlHandler htmlHandler = new ViewerHtmlHandler(config);

            HtmlOptions options = new HtmlOptions();
            options.setEmbedResources(true);
            List<PageHtml> pages = htmlHandler.getPages(fileName, options);

            for (PageHtml page : pages) {
                return Utilities.saveAsHtml(page.getPageNumber() + "_" + fileName, page.getHtmlContent());
            }
        } catch (Exception exp) {
            System.out.println("Exception: " + exp.getMessage());
            exp.printStackTrace();
        }
        return fileName;
    }
}
