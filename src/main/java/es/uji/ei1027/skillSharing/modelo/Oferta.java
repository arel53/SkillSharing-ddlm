package es.uji.ei1027.skillSharing.modelo;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Oferta {
    private int idOferta;
    private String estudiante;
    private int horas;
    private Date iniFecha;
    private Date finFecha;
    private boolean activa;
    private String skill;
    private String nivel;
    private String descripcion;
    static private final AtomicInteger a = new AtomicInteger();


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

    public void setIniFecha(Date iniFecha) {
        this.iniFecha = iniFecha;
    }

    public void setFinFecha(Date finFecha) {
        this.finFecha = finFecha;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAndIncrement(){ return a.getAndIncrement(); }
    public int getAndDecrement(){ return a.getAndDecrement(); }

    public int getIdOferta() {
        return idOferta;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public int getHoras() {
        return horas;
    }

    public Date getIniFecha() {
        return iniFecha;
    }

    public Date getFinFecha() {
        return finFecha;
    }

    public boolean isActiva() {
        return activa;
    }

    public String getSkill() {
        return skill;
    }

    public String getNivel() {
        return nivel;
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
                ", nivel='" + nivel + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
