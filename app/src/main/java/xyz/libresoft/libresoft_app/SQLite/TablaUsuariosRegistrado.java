package xyz.libresoft.libresoft_app.SQLite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by azulm on 07/05/2018.
 */

public class TablaUsuariosRegistrado extends SQLiteOpenHelper{


    public TablaUsuariosRegistrado(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table Usuario(correo varchar(50),contrasena varchar(20))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
