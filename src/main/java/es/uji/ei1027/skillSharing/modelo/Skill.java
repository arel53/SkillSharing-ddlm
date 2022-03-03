package es.uji.ei1027.skillSharing.modelo;

public class Skill {
    private String nombre;
    private Boolean activo;

    //Quitado el constructor

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() { return activo; }

    @Override
    public String toString() {
        return "Skill{" +
                "nombre='" + nombre + '\'' +
                ", nivel='" + activo + '\'' +
                '}';
    }
}
