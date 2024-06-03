package modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Categoria {
    private String nombre;
    private int id;
    private byte[] imagen;
    private String color;

    public Categoria( int id,String nombre, byte[] imagen, String color) {
        this.nombre = nombre;
        this.id = id;
        this.imagen = imagen;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public Bitmap getBitmap(){
        return BitmapFactory.decodeByteArray(this.imagen, 0, imagen.length);
    }
    public int getIntColor() {
        try {

            return Color.parseColor(this.color);
        } catch (IllegalArgumentException e) {

            return Color.BLACK;
        }
    }
}
