package gaf.controller;

import gaf.entity.Corte;
import gaf.entity.Talle;
import gaf.service.AttachService;
import gaf.service.CorteService;
import gaf.service.EstadoService;
import gaf.service.TalleService;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ViewScoped
@ManagedBean(name = "corteList")
public class CorteListController {

    private List<Corte> cortes;
    private Integer corteId; // Para los attach

    @EJB private AttachService attachService;
    @EJB private CorteService corteService;
    @EJB private TalleService talleService;
    @EJB private EstadoService estadoService;

    @PostConstruct
    public void init() {
        cortes = corteService.findAll();
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
    }

    public Integer getCorteId() {
        return corteId;
    }

    public void setCorteId(Integer corteId) {
        this.corteId = corteId;
    }

    public String generateCorteLabelForPanel(Corte corte) {
        String label = corte.getName();
        if (corte.getPrice() != null) label += "|$" + corte.getPrice();
        label += "|Cant. de prendas: " + corte.getClothesQuantity();
        return label;
    }

    public List<Talle> getTallesOfCorte(Integer id) {
        return talleService.findTallesFromCorte(id);
    }

    public String getColorForCorte(Corte corte) {
        return estadoService.findById(corte.getEstadoId()).getColor();
    }

    public void getAttachsByCorteId(Corte corte) {
        corteId = corte.getId();
        /*
        List<Attach> attachs = attachService.findByCorteId(corte.getId());
        if (CollectionUtils.isNotEmpty(attachs)) {
            images = new ArrayList<>();
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
        */
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlg2').show();");
    }
}
