package gaf.util;

public enum Estados {
    TALLER_ARREGLANDO(1, "TALLER", "Arreglando", "yellow"),
    TALLER_DISPONIBLE(2, "TALLER", "Disponible", "green"),
    TALLER_EN_DEUDA(3, "TALLER", "En deuda", "blue"),
    TALLER_EN_PRODUCCION(4, "TALLER", "En produccion", "orange"),
    TALLER_NO_DISPONIBLE(5, "TALLER", "No disponible", "red"),
    CORTE_SIN_ASIGNAR(6, "CORTE", "Sin asignar", ""),
    CORTE_EN_PRODUCCION(7, "CORTE", "En produccion", ""),
    CORTE_CERRADO_CON_DEUDA(8, "CORTE", "Cerrado con deudas", ""),
    CORTE_FINALIZADO(9, "CORTE", "Finalizado", "");

    private Integer id;
    private String entity;
    private String name;
    private String color;

    Estados(Integer id, String entity, String name, String color) {
        this.id = id;
        this.entity = entity;
        this.name = name;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public String getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}