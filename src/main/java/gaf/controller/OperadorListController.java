package gaf.controller;

import gaf.entity.Estado;
import gaf.entity.Operador;
import gaf.service.OperadorService;
import gaf.util.Estados;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "operadorList")
public class OperadorListController {

    private List<Operador> operadores;
    @EJB private OperadorService operadorService;

    @PostConstruct
    public void init() {
    }

    public List<Operador> getOperadores() {
        if (operadores == null) {
            operadores = operadorService.findAll();
        }
        return operadores;
    }

    public void setOperadores(List<Operador> operadores) {
        this.operadores = operadores;
    }
}
