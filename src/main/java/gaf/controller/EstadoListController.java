package gaf.controller;

import gaf.entity.Estado;
import gaf.util.Estados;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "estadoList")
public class EstadoListController {

    private List<Estado> estados;

    @PostConstruct
    public void init() {
        estados = new ArrayList<>();
        for (Estados e : Estados.values()) {
            Estado estado = new Estado();
            estado.setId(e.getId());
            estado.setName(e.getName());
            estado.setColor(e.getColor());
            estado.setEntity(e.getEntity());
            estados.add(estado);
        }
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }
}
