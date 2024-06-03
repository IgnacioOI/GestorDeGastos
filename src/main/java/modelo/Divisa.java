package modelo;

public class Divisa {
    private int id;
    private String nombre;
    private String simbolo;

    public Divisa(int id, String nombre, String simbolo) {
        this.id = id;
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
