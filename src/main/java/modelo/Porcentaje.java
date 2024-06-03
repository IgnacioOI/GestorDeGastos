package modelo;

import android.graphics.Color;

public class Porcentaje {
    private String nombreCategoria;
    private String color;
    private double porcentaje;


    public Porcentaje(String nombreCategoria, String color, double porcentaje) {
        this.nombreCategoria = nombreCategoria;
        this.color = color;
        this.porcentaje = porcentaje;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    public int getIntColor() {
        try {

            return Color.parseColor(this.color);
        } catch (IllegalArgumentException e) {

            return Color.BLACK;
        }
    }
}
