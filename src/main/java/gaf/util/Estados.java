package gaf.util;

public enum Estados {
    TALLER_ARREGLANDO       (1 , "TALLER", "Arreglando"         , "navy"),
    TALLER_DISPONIBLE       (2 , "TALLER", "Disponible"         , "lime"),
    TALLER_EN_PRODUCCION    (3 , "TALLER", "En produccion"      , "blue"),
    TALLER_NO_DISPONIBLE    (4 , "TALLER", "No disponible"      , "maroon"),
    CORTE_SIN_ASIGNAR       (5 , "CORTE" , "Sin asignar"        , ""),
    CORTE_EN_PRODUCCION     (6 , "CORTE" , "En produccion"      , "blue"),
    CORTE_CERRADO_CON_DEUDA (7 , "CORTE" , "Cerrado con deudas" , "yellow"),
    CORTE_FINALIZADO        (8 , "CORTE" , "Finalizado"         , "olive"),
    TALLE_PAGADO            (9 , "TALLE" , "Pagado"             , ""),
    TALLE_NO_PAGADO         (10, "TALLE" , "No pagado"          , ""),
    TALLE_PENDIENTE         (11, "TALLE" , "Pendiente"          , "");

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