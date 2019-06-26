package xyz.libresoft.libresoft_app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xyz.libresoft.libresoft_app.Clases.Conexion;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosRegistrado;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Conexion.isConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"No hay internet para usar la app",Toast.LENGTH_SHORT).show();
                        return;
                }

                TablaUsuariosRegistrado cnx = new TablaUsuariosRegistrado(getApplicationContext(), "UsuarioRegistrado", null, 1);
                SQLiteDatabase db = cnx.getWritableDatabase();
                final Cursor datos = db.rawQuery(
                        "SELECT * FROM Usuario",null
                );
                boolean sino = true;
                while (datos.moveToNext()) {
//
                    String   url = "http://libresoft.000webhostapp.com/volley/ObtenerUsuario.php?correo="+datos.getString(0)+"&contra="+datos.getString(1);
                    DatosUsuarioLocal.Contraseña=datos.getString(1);
                    url=url.replace(" ","%20");
                    url = url.replace(" ", "%20");
                    Log.i("AQUI",datos.getString(1));

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONArray json = response.optJSONArray("usuario");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = null;
                                    jsonObject = json.getJSONObject(i);
                                    Intent j = new Intent(getApplicationContext(), MainActivity.class);
                                    DatosUsuarioLocal.Ciudad=jsonObject.optString("Ciudad");
                                    DatosUsuarioLocal.CiudadParaBuscar=jsonObject.optString("Ciudad");
                                    DatosUsuarioLocal.EstadoParaBuscar=jsonObject.optString("Estado");
                                    DatosUsuarioLocal.Correo=jsonObject.optString("Correo");
                                    DatosUsuarioLocal.CUIL=jsonObject.optString("CUIL");
                                    DatosUsuarioLocal.Estado=jsonObject.optString("Estado");
                                    DatosUsuarioLocal.Nombre=jsonObject.optString("Nombre");
                                    DatosUsuarioLocal.Codigo_Usuario=jsonObject.optString("Codigo_Usuario");
                                    DatosUsuarioLocal.Tipo=jsonObject.optString("Tipo");
                                    DatosUsuarioLocal.bd=jsonObject.optString("Lugar");

                                    startActivity(j);
                                }
                            }catch(Exception e){


                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                            error.printStackTrace();

                        }
                    });
                    queue.add(stringRequest);
                    //
                    sino=false;
                }
                db.close();
                if(sino) {
                    Intent i = new Intent(getApplicationContext(), IngresarDos.class);
                    startActivity(i);
                }
            }
        },1500);
    }
}
