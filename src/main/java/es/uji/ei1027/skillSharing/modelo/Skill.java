package es.uji.ei1027.skillSharing.modelo;

public class Skill {
    private int idSkill;
    private String nombre;
    private boolean activo;
    private String nivel;
    private String descripcion;


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

    @Override
    public String toString() {
        return "Skill{" +
                "nombre='" + nombre + '\'' +
                ", nivel='" + activo + '\'' +
                '}';
    }
}
