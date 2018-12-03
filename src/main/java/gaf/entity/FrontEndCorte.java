package gaf.entity;

public class FrontEndCorte {

    private Corte corte;
    private String frontEndLabel;

    public FrontEndCorte(Corte corte, String frontEndLabel) {
        this.corte = corte;
        this.frontEndLabel = frontEndLabel;
    }

    public Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    public String getFrontEndLabel() {
        return frontEndLabel;
    }

    public void setFrontEndLabel(String frontEndLabel) {
        this.frontEndLabel = frontEndLabel;
    }

    @Override
    public String toString() {
        return "FrontEndCorte ["
                + ((corte != null) ? "corte=" + corte + ", " : "")
                + ((frontEndLabel != null) ? "frontEndLabel=" + frontEndLabel : "")
                + "]";
    }
}
