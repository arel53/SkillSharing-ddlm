package es.uji.ei1027.skillSharing.modelo;

public class Usuario {
    private String username;
    private String password;
    private boolean skp;
    private boolean active;
    private String nif;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", skp=" + skp +
                ", active=" + active +
                ", nif='" + nif + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
