package es.uji.ei1027.skillSharing.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Colaboracion {

    private int idColaboracion;
    private int idOferta;
    private int idDemanda;
    private String rutaImgOfertante;
    private String rutaImgDemandante;
    private String rutaImgSkill;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate iniFecha;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate finFecha;
    private boolean activa;
    private int rate;
    private String comentario;
    private int horas;
    private String nombreApellidoOfertante;
    private String nombreApellidoDemandante;
    private String nifDemandante;
    private String nifOfertante;


    private String skill;

    public Colaboracion() {
        super();
    }

    public String getRutaImgOfertante() {
        return rutaImgOfertante;
    }
    public String getRutaImgDemandante() {
        return rutaImgDemandante;
    }
    public String getRutaImgSkill() {
        return rutaImgSkill;
    }
    public int getIdColaboracion() {
        return idColaboracion;
    }

    public int getIdOferta() {
        return idOferta;
    }

    public int getIdDemanda() {
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
    public void setRutaImgOfertante(String rutaImgOfertante) {
        this.rutaImgOfertante = rutaImgOfertante;
    }
    public void setRutaImgDemandante(String rutaImgDemandante) {
        this.rutaImgDemandante = rutaImgDemandante;
    }
    public String getNombreApellidoOfertante() {
        return nombreApellidoOfertante;
    }

    public String getNombreApellidoDemandante() {
        return nombreApellidoDemandante;
    }


    public String getNifDemandante() {
        return nifDemandante;
    }

    public String getSkill() {return skill;}

    public void setRutaImgSkill(String rutaImgSkill) {
        this.rutaImgSkill = rutaImgSkill;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }
    public void setIdOferta(int idOferta) {
        this.idOferta = idOferta;
    }

    public void setIdDemanda(int idDemanda) {
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

    public void setNombreApellidoOfertante(String nombreApellidoOfertante) {
        this.nombreApellidoOfertante = nombreApellidoOfertante;
    }

    public void setNombreApellidoDemandante(String nombreApellidoDemandante) {
        this.nombreApellidoDemandante = nombreApellidoDemandante;
    }

    public void setNifDemandante(String nifDemandante) {
        this.nifDemandante = nifDemandante;
    }


    public void setSkill(String skill) {this.skill = skill;}

    public String getNifOfertante() {
        return nifOfertante;
    }

    @Override
    public String toString() {
        return "Colaboracion{" +
                "idColaboracion=" + idColaboracion +
                ", idOferta=" + idOferta +
                ", idDemanda=" + idDemanda +
                ", rutaImgOfertante='" + rutaImgOfertante + '\'' +
                ", rutaImgDemandante='" + rutaImgDemandante + '\'' +
                ", rutaImgSkill='" + rutaImgSkill + '\'' +
                ", iniFecha=" + iniFecha +
                ", finFecha=" + finFecha +
                ", activa=" + activa +
                ", rate=" + rate +
                ", comentario='" + comentario + '\'' +
                ", horas=" + horas +
                ", nombreApellidoOfertante='" + nombreApellidoOfertante + '\'' +
                ", nombreApellidoDemandante='" + nombreApellidoDemandante + '\'' +
                ", nifDemandante='" + nifDemandante + '\'' +
                ", nifOfertante='" + nifOfertante + '\'' +
                ", skill='" + skill + '\'' +
                '}';
    }

    public void setNifOfertante(String nifOfertante) {
        this.nifOfertante = nifOfertante;
    }

}
