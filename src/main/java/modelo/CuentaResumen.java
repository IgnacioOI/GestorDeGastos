package modelo;

public class CuentaResumen {
    private int idCuenta;
    private String nombre;
    private double saldoTotal;

    public CuentaResumen(int idCuenta, String nombre, double saldoTotal) {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.saldoTotal = saldoTotal;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldoTotal() {
        return saldoTotal;
    }
}
