package es.uji.ei1027.skillSharing.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Oferta{
    private int idOferta;
    private String estudiante;
    private int horas;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate iniFecha;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate finFecha;
    private boolean activa;
    private int skill;
    private String descripcion;


    public Oferta(){

    }
    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
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

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdOferta() {
        return idOferta;
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

    public int getSkill() {
        return skill;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Oferta{" +
                "idOferta=" + idOferta +
                ", estudiante='" + estudiante + '\'' +
                ", horas=" + horas +
                ", iniFecha=" + iniFecha +
                ", finFecha=" + finFecha +
                ", activa=" + activa +
                ", skill='" + skill + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
