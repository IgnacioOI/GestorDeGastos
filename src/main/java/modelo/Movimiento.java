package modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Date;

public class Movimiento implements Parcelable {
    private int idMovimiento;
    private int idCuenta;
    private int idUsuario;
    private String tipoMovimiento;
    private int idCatecoria;
    private String categoria;
    private Date fecha;
    private String descripcion;

    private byte[] imagen;
    private double cantidad;

    private String colorCategoria;
    private String divisa;

    public Movimiento(int idMovimiento, int idCuenta, int idUsuario, String tipoMovimiento, int idCatecoria, String categoria, Date fecha, String descripcion, byte[] imagen, double cantidad, String colorCategoria) {
        this.idMovimiento = idMovimiento;
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.idCatecoria = idCatecoria;
        this.categoria = categoria;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.colorCategoria = colorCategoria;
    }

    public Movimiento(int idMovimiento, int idCuenta, int idUsuario, String tipoMovimiento, int idCatecoria, String categoria, Date fecha, String descripcion, byte[] imagen, double cantidad, String colorCategoria,String divisa) {
        this.idMovimiento = idMovimiento;
        this.idCuenta = idCuenta;
        this.idUsuario = idUsuario;
        this.tipoMovimiento = tipoMovimiento;
        this.idCatecoria = idCatecoria;
        this.categoria = categoria;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.colorCategoria = colorCategoria;
        this.divisa=divisa;
    }
    protected Movimiento(Parcel in) {
        idMovimiento = in.readInt();
        idCuenta = in.readInt();
        idUsuario = in.readInt();
        tipoMovimiento = in.readString();
        idCatecoria = in.readInt();
        categoria = in.readString();
        long tmpFecha = in.readLong();
        fecha = tmpFecha != -1 ? new Date(tmpFecha) : null;
        descripcion = in.readString();
        imagen = in.createByteArray();
        cantidad = in.readDouble();
        colorCategoria = in.readString();
        divisa = in.readString();
    }

    public static final Creator<Movimiento> CREATOR = new Creator<Movimiento>() {
        @Override
        public Movimiento createFromParcel(Parcel in) {
            return new Movimiento(in);
        }

        @Override
        public Movimiento[] newArray(int size) {
            return new Movimiento[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idMovimiento);
        dest.writeInt(idCuenta);
        dest.writeInt(idUsuario);
        dest.writeString(tipoMovimiento);
        dest.writeInt(idCatecoria);
        dest.writeString(categoria);
        dest.writeLong(fecha != null ? fecha.getTime() : -1);
        dest.writeString(descripcion);
        dest.writeByteArray(imagen);
        dest.writeDouble(cantidad);
        dest.writeString(colorCategoria);
        dest.writeString(divisa);
    }

    public Movimiento(String colorCategoria) {
        this.colorCategoria = colorCategoria;
    }

    public double getCantidad() {
        return cantidad;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public int getIdCatecoria() {
        return idCatecoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getColorCategoria() {
        return colorCategoria;
    }

    public byte[] getImagen() {
        return imagen;
    }
    public Bitmap getBitmap(){
        return BitmapFactory.decodeByteArray(this.imagen, 0, imagen.length);
    }

    public int getIntColor() {
        try {

            return Color.parseColor(this.colorCategoria);
        } catch (IllegalArgumentException e) {

            return Color.BLACK;
        }
    }

    public String getDivisa() {
        return divisa;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "idMovimiento=" + idMovimiento +
                ", idCuenta=" + idCuenta +
                ", idUsuario=" + idUsuario +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", idCatecoria=" + idCatecoria +
                ", categoria='" + categoria + '\'' +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", imagen=" + Arrays.toString(imagen) +
                ", cantidad=" + cantidad +
                ", colorCategoria='" + colorCategoria + '\'' +
                '}';
    }
}
