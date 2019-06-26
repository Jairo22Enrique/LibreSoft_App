package xyz.libresoft.libresoft_app.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by CHENAO on 6/08/2017.
 */

public class ElemenComentarios {

private String TituloReseña;
    private String Reseña;
    private String Estrellas;
    private String Fecha;
    private String NombreUsuario;
    private String TipoUsuario;


    public String getTituloReseña() {
        return TituloReseña;
    }

    public void setTituloReseña(String tituloReseña) {
        TituloReseña = tituloReseña;
    }

    public String getReseña() {
        return Reseña;
    }

    public void setReseña(String reseña) {
        Reseña = reseña;
    }

    public String getEstrellas() {
        return Estrellas;
    }

    public void setEstrellas(String estrellas) {
        Estrellas = estrellas;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getTipoUsuario() {
        return TipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        TipoUsuario = tipoUsuario;
    }
}