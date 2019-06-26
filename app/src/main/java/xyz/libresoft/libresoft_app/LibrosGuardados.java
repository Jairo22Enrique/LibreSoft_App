package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.libresoft.libresoft_app.Adaptadores.Adaptador;
import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorLibrosGuardados;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class LibrosGuardados extends AppCompatActivity {
    RecyclerView rvLibrosGuardados;
    ArrayList<Elemen> listaReservas2;
    TextView txtText;
    AdaptadorLibrosGuardados adapter = null;
    ProgressBar pg;
    int no =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros_guardados);
        try {
            getSupportActionBar().hide();
            Toolbar toolbar = (Toolbar) findViewById(R.id.bar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.flechaazulp));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }); toolbar.setTitle("Libros guardados");
            toolbar.setTitleTextColor(Color.WHITE);
            listaReservas2 = new ArrayList<>();

            adapter = new AdaptadorLibrosGuardados(listaReservas2);
            txtText=findViewById(R.id.txtTexto);
//recycler
            rvLibrosGuardados = findViewById(R.id.rvLibrosGuardados);
            rvLibrosGuardados.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvLibrosGuardados.setLayoutManager(llm);
            rvLibrosGuardados.setItemAnimator(new DefaultItemAnimator());
            pg = findViewById(R.id.progress2);
            rvLibrosGuardados.setVisibility(View.GONE);
            Enviar();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        /*
            e=findViewById(R.id.e);
            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    db.delete("Guardados","",null);
                    db.delete("Lugares_Favoritos","",null);
                }
            });
            tv = findViewById(R.id.guardados);
            TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            Cursor datos = db.rawQuery(
                    "SELECT * FROM Guardados", null
            );

            while (datos.moveToNext()) {
                tv.setText(tv.getText().toString() + "\n" + datos.getString(0) + " : " + datos.getString(1) + " : " + datos.getString(2));
            }
             datos = db.rawQuery(
                    "SELECT * FROM Lugares_Favoritos", null
            );

            while (datos.moveToNext()) {
                tv.setText(tv.getText().toString() + "\n" + datos.getString(0) + " : " + datos.getString(1) + " : " + datos.getString(2));
            }

        */
    }

    void Enviar(){
        try {
            TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            final Cursor datos = db.rawQuery(
                    "SELECT * FROM Guardados WHERE codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario + "'", null
            );
            boolean sino=true;
            while (datos.moveToNext()) {
sino=false;
                String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + datos.getString(1) + "&donde=codigo&tipo=geneal&order=calificacion";

                Log.i("AQUI", url);
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Elemen usuario = null;
                            JSONArray json = response.optJSONArray("usuario");
//---------------------
                 /*   rvLibrosGuardados.removeAllViewsInLayout();
                    final int size = listaReservas2.size();
                    listaReservas2.clear();
                    adapter.notifyItemRangeRemoved(0, size);*/
//---------------------

                            for (int i = 0; i < json.length(); i++) {
                                usuario = new Elemen();
                                if (listaReservas2.size() > 9) {
                                    continue;
                                }
                                JSONObject jsonObject = null;
                                jsonObject = json.getJSONObject(i);
                                usuario.setTitulo(jsonObject.optString("Titulo"));
                                usuario.setAutor(jsonObject.optString("Autor"));
                                usuario.setGenero(jsonObject.optString("Genero"));
                                usuario.setDato(jsonObject.optString("Imagen"));
                                usuario.setEditorial(jsonObject.optString("Editorial"));
                                usuario.setPrecio(jsonObject.optString("Precio"));
                                usuario.setLugar(jsonObject.optString("Lugar"));
                                usuario.setDescipcion(jsonObject.optString("Descripcion"));
                                usuario.setAno(jsonObject.optString("Ano"));
                                usuario.setEdicion(jsonObject.optString("Edicion"));
                                usuario.setEstrellas(jsonObject.optString("Estrellas"));
                                usuario.setCodigo(jsonObject.optString("Codigo"));
                                //
                                usuario.setUnidades(jsonObject.optString("Unidades"));
                                usuario.setEstante(jsonObject.optString("Estante"));
                                usuario.setBuscandoEn(DatosUsuarioLocal.bd);
                                Log.i("NOMBRE", jsonObject.optString("Nombre"));
                                listaReservas2.add(usuario);
                                //  Adaptador adapter = new Adaptador(listaReservas2);
                                rvLibrosGuardados.setAdapter(adapter);
                            }


                            if (listaReservas2.size() == 0) {
                                usuario.setTitulo("No se han encontrado libros relacionados.");
                                listaReservas2.add(usuario);
                                //     adapter = new Adaptador(listaReservas2);
                                rvLibrosGuardados.setAdapter(adapter);
                            }
                            pg.setVisibility(View.GONE);
                            rvLibrosGuardados.setVisibility(View.VISIBLE);
                            no++;
                        } catch (Exception e) {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_SHORT).show();

                        error.printStackTrace();
                    }
                });
                queue.add(stringRequest);
            }
            if(sino){
                pg.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No tienes ningun libro agregado a tu favoritos.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
