package es.uji.ei1027.skillSharing.modelo;

public class Niveles {

    private String nombre;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Niveles{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
