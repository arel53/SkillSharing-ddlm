package es.uji.ei1027.skillSharing.modelo;

import java.awt.image.BufferedImage;

public class Skill {
    private int idSkill;
    private String nombre;
    private boolean activo;
    private String nivel;
    private String descripcion;
    private int numeroOfertas;
    private int numeroDemandas;

    public String getRutaim() {
        return rutaim;
    }

    public void setRutaim(String rutaim) {
        this.rutaim = rutaim;
    }

    private String rutaim;


    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public Skill(){

    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setIdSkill(int idSkill) {
        this.idSkill = idSkill;
    }


    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public void setNumeroOfertas(int numeroOfertas) {this.numeroOfertas = numeroOfertas;    }

    public void setNumeroDemandas(int numeroDemandas) {this.numeroDemandas = numeroDemandas;}

    public int getIdSkill() {
        return idSkill;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() { return activo; }

    public String getNivel() {
        return nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNumeroOfertas() {
        return numeroOfertas;
    }

    public int getNumeroDemandas() {
        return numeroDemandas;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "nombre='" + nombre + '\'' +
                ", nivel='" + activo + '\'' +
                '}';
    }
}
