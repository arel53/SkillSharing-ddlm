package es.uji.ei1027.skillSharing.modelo;

public class Estudiante {
    private String nif;
    private String nombre;
    private String apellido;
    private String email;
    private String grado;
    private int edad;
    private String sexo;
    private String direccion;
    private int horas;
    private String rutaimg;

    public String getRutaimg() {
        return rutaimg;
    }

    public void setRutaimg(String rutaimg) {
        this.rutaimg = rutaimg;
    }

    public Estudiante(){
        super();
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getNif() {
        return nif;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getGrado() {
        return grado;
    }

    public int getEdad() {
        return edad;
    }

    public String getSexo() {
        return sexo;
    }


    public String getDireccion() {
        return direccion;
    }

    public int getHoras() {
        return horas;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nif='" + nif + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", grado='" + grado + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", horas=" + horas +
                ", rutaimg='" + rutaimg + '\'' +
                '}';
    }
}
