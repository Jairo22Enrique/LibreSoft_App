package xyz.libresoft.libresoft_app.SQLite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by azulm on 07/05/2018.
 */

public class TablaUsuariosDatos extends SQLiteOpenHelper{

    public TablaUsuariosDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Guardados(codigo_usuario int(3) ,codigo_libro varchar(20),fecha varchar(30))"
        );
        db.execSQL(
                "create table Lugares_Favoritos(codigo_usuario int(3) ,nombre_bd varchar(50),fecha varchar(30))"
        );
        db.execSQL(
                "create table Preferencias(codigo_usuario int(3) ,ultimo_genero_consultado varchar(70),ultimo_titulo_consultado varchar(100))"
        );
        db.execSQL(
                "create table Consultados(codigo_usuario int(3) ,genero varchar(70),titulo varchar(70))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
