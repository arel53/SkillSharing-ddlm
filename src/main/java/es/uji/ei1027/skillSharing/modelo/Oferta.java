package es.uji.ei1027.skillSharing.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Oferta{
    private int idOferta;
    private String rutaImg;
    private String rutaImgSkill;
    private String estudiante;
    private int horas;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate iniFecha;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate finFecha;
    private boolean activa;
    private int skill;
    private String nombreSkill;
    private String  nivelSkill;
    private String descripcion;


    public Oferta(){

    }



    public void setRutaImgSkill(String rutaImgSkill) {
        this.rutaImgSkill = rutaImgSkill;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
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

    public void setNombreSkill(String nombreSkill) {
        this.nombreSkill = nombreSkill;
    }

    public void setNivelSkill(String nivelSkill) {
        this.nivelSkill = nivelSkill;
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

    public String getRutaImgSkill() {
        return rutaImgSkill;
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

    public String getNombreSkill() {
        return nombreSkill;
    }

    public String getNivelSkill() {
        return nivelSkill;
    }

    public String getRutaImg() {
        return rutaImg;
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
                ", skill=" + skill +
                ", nombreSkill='" + nombreSkill + '\'' +
                ", nivelSkill='" + nivelSkill + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}