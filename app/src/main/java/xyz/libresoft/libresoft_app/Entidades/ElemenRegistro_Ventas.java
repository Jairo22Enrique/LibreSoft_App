package xyz.libresoft.libresoft_app.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by azulm on 11/05/2018.
 */

public class ElemenRegistro_Ventas {

    private String Titulo;
    private String Autor;
    private String Vendidos;
    private String Fecha_venta;
    private String Hora_venta;
    private String Vendedor;
    private Bitmap imagen;
    private String dato;
    private String Total;

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getDato() {
        return dato;
    }
    public void setDato(String dato) {
        this.dato = dato;

        try {
            byte[] byteCode= Base64.decode(dato, Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles
            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen= Bitmap.createScaledBitmap(foto,alto,ancho,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public String getVendidos() {
        return Vendidos;
    }

    public void setVendidos(String vendidos) {
        Vendidos = vendidos;
    }

    public String getFecha_venta() {
        return Fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        Fecha_venta = fecha_venta;
    }

    public String getHora_venta() {
        return Hora_venta;
    }

    public void setHora_venta(String hora_venta) {
        Hora_venta = hora_venta;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }
}
