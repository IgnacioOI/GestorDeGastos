package modelo;

import java.sql.Blob;

public class Cuenta {
    private int idCuenta;
    private int idUsuario;
    private String nombre;
    //private Blob icono;
    private float saldo;
    private char divisa;

    public Cuenta(int idCuenta, int idUsuario, String nombre,  float saldo, char divisa) {
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
       // this.icono = icono;
        this.saldo = saldo;
        this.divisa = divisa;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

   // public Blob getIcono() {
    //    return icono;
    //}

    public float getSaldo() {
        return saldo;
    }

    public char getSimbolo() {
        return divisa;
    }
}
