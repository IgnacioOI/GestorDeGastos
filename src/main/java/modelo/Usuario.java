package modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String contrasena;

    public Usuario(String nombre, String contrasena,int id) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

}
