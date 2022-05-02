package es.uji.ei1027.skillSharing.modelo;

import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class Colaboracion {

    private int idOferta;
    private String idDemanda;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate iniFecha;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate finFecha;
    private boolean activa;
    private int rate;
    private String comentario;
    private int horas;


    public Colaboracion() {
        super();
    }

    public int getIdOferta() {
        return idOferta;
    }

    public String getIdDemanda() {
        return idDemanda;
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

    public int getRate() {
        return rate;
    }

    public String getComentario() {
        return comentario;
    }

    public int getHoras() {
        return horas;
    }

    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public void setIdDemanda(String idDemanda) {
        this.idDemanda = idDemanda;
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

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    @Override
    public String toString() {
        return "Colaboracion{" +
                "idOferta='" + idOferta + '\'' +
                ", idDemanda='" + idDemanda + '\'' +
                ", iniFecha=" + iniFecha +
                ", finFecha=" + finFecha +
                ", rate=" + rate +
                ", comentario='" + comentario + '\'' +
                ", horas=" + horas +
                '}';
    }
}
