package xyz.libresoft.libresoft_app.Entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by CHENAO on 6/08/2017.
 */

public class Elemen {

    public String getDato() {
        return dato;
    }

    private String Titulo;
    private String dato;
    private String Editorial;
    private String Precio;
    private String Unidades;

    public String getBuscandoEn() {
        return BuscandoEn;
    }

    public void setBuscandoEn(String buscandoEn) {
        BuscandoEn = buscandoEn;
    }

    private String BuscandoEn;

    public String getUnidades() {
        return Unidades;
    }

    public void setUnidades(String unidades) {
        Unidades = unidades;
    }

    public String getEstante() {
        return Estante;
    }

    public void setEstante(String estante) {
        Estante = estante;
    }

    private String Estante;

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    private String Lugar;
private String Estrellas;
    private String Codigo;
    public String getAno() {
        return Ano;
    }

    public String getEstrellas() {
        return Estrellas;
    }

    public void setEstrellas(String estrellas) {
        Estrellas = estrellas;
    }

    public void setAno(String ano) {
        Ano = ano;
    }

    public String getEdicion() {
        return Edicion;
    }

    public void setEdicion(String edicion) {
        Edicion = edicion;
    }

    private String Ano;
    private String Edicion;

    public String getDescipcion() {
        return Descipcion;
    }

    public void setDescipcion(String descipcion) {
        Descipcion = descipcion;
    }

    private Bitmap imagen;
    private String Descipcion;

    public String getEditorial() {
        return Editorial;
    }

    public void setEditorial(String editorial) {
        Editorial = editorial;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
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

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    private String Autor;
private String Genero;

}
