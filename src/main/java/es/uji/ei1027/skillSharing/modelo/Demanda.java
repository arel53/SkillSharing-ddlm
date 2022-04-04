package es.uji.ei1027.skillSharing.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Demanda {
    private int idDemanda;
    private String estudiante;
    private int horas;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate iniFecha;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate finFecha;
    private boolean activa;
    private int idSkill;
    private String descripcion;

    public Demanda(){
        super();
    }


    public void setIdDemanda(int idDemanda) {
        this.idDemanda = idDemanda;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public void setIniFecha(LocalDate iniFecha) {
        this.iniFecha = iniFecha;
    }

    public void setFinFecha(LocalDate finFecha) {
        this.finFecha = finFecha;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public void setIdSkill(int idSkill) {
        this.idSkill = idSkill;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdDemanda() {
        return idDemanda;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public int getHoras() {
        return horas;
    }

    public LocalDate getIniFecha() {
        return iniFecha;
    }

    public LocalDate getFinFecha() {
        return finFecha;
    }

    public boolean isActiva() {
        return activa;
    }

    public int getIdSkill() {
        return idSkill;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Demanda{" +
                "idDemanda=" + idDemanda +
                ", estudiante='" + estudiante + '\'' +
                ", horas=" + horas +
                ", iniFecha=" + iniFecha +
                ", finFecha=" + finFecha +
                ", activa=" + activa +
                ", idSkill='" + idSkill + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
