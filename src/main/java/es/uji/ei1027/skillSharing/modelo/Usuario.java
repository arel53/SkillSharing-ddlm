package es.uji.ei1027.skillSharing.modelo;

public class Usuario {
    private String username;
    private String password;
    private boolean skp;
    private boolean active;
    private String NIF;
    private String descripcion;

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSkp() {
        return skp;
    }

    public void setSkp(boolean skp) {
        this.skp = skp;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
