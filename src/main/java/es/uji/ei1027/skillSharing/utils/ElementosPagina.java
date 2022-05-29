package es.uji.ei1027.skillSharing.utils;

public class ElementosPagina {
    private int numero;
    private boolean actual;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public ElementosPagina(int numero, boolean actual) {
        this.numero = numero;
        this.actual = actual;
    }
    public ElementosPagina(){
        super();
    }
}
