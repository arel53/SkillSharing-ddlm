package es.uji.ei1027.skillSharing.modelo;

public class Skill {
    private String nombre;
    private String nivel;

    public Skill(){}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "nombre='" + nombre + '\'' +
                ", nivel='" + nivel + '\'' +
                '}';
    }
}
