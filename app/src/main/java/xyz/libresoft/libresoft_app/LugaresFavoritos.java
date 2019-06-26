package xyz.libresoft.libresoft_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorLugaresGeneral;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.Mapas.MapasLugaresGeneralGeneral;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class LugaresFavoritos extends AppCompatActivity {
    RecyclerView rvLugaresFavoritos;
    ArrayList<ElemenBusarLugares> ListaLugares;
    AdaptadorLugaresGeneral adapter;
    RelativeLayout content;
    ProgressBar pg;
    ImageButton btnMapa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugares_favoritos);
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
            });

            ListaLugares = new ArrayList<>();
            adapter = new AdaptadorLugaresGeneral(ListaLugares);
            rvLugaresFavoritos = findViewById(R.id.rvLugaresFavoritos);
            rvLugaresFavoritos.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvLugaresFavoritos.setLayoutManager(llm);
            pg = findViewById(R.id.progress);
            content = findViewById(R.id.content);
            content.setVisibility(View.GONE);
            toolbar.setTitle("Lugares favoritos");
            toolbar.setTitleTextColor(Color.WHITE);
            CargarLugares();
            btnMapa = findViewById(R.id.btnMapa);
            btnMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("TAMAÑO LISTA", ListaLugares.size() + "");
                        if(ListaLugares.size()==0){
                            Toast.makeText(getApplicationContext(),"No hay lugares para mostrar",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] Nombre = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Nombre[i] = ListaLugares.get(i).getNombreLugar();
                        }
                        String[] Direccion = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Direccion[i] = ListaLugares.get(i).getDireccion();
                        }
                        String[] Latitud = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Latitud[i] = ListaLugares.get(i).getLatitud();
                        }
                        String[] Longuitud = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Longuitud[i] = ListaLugares.get(i).getLonguitud();
                        }
                        String[] Version = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Version[i] = ListaLugares.get(i).getTipo();
                        }
                        String[] Eslogan = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Eslogan[i] = ListaLugares.get(i).getEslogan();
                        }
                        String[] Tipo = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Tipo[i] = ListaLugares.get(i).getTipo();
                        }
                        String[] Correo = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Correo[i] = ListaLugares.get(i).getCorreo();
                        }
                        String[] Tel = new String[ListaLugares.size()];
                        for (int i = 0; i < ListaLugares.size(); i++) {
                            Tel[i] = ListaLugares.get(i).getTelefono();
                        }

                        Intent i = new Intent(getApplicationContext(), MapasLugaresGeneralGeneral.class);
                        i.putExtra("Nombre", Nombre);
                        i.putExtra("Direccion", Direccion);
                        i.putExtra("Latitud", Latitud);
                        i.putExtra("Longuitud", Longuitud);
                        i.putExtra("Version", Version);
                        i.putExtra("Eslogan", Eslogan);
                        i.putExtra("Tipo", Tipo);
                        i.putExtra("Correo", Correo);
                        i.putExtra("Tel", Tel);
                        startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"No hay lugares para mostrar",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    void CargarLugares(){
        try{
            TablaUsuariosDatos cnx = new TablaUsuariosDatos(this, "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            Cursor datos = db.rawQuery(
                    "SELECT * FROM Lugares_Favoritos WHERE codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario+"'", null
            );
            boolean sino=true;
        while (datos.moveToNext()) {
sino=false;
            String url = "https://libresoft.000webhostapp.com/volley/ObtenerLugarNombre.php?nombre="+datos.getString(1);
            url = url.replace(" ","%20");
            Log.i("AQUI",url);
            Log.i("AQUI",datos.getString(1));
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                   /*     rvLugaresFavoritos.removeAllViewsInLayout();
                        final int size = ListaLugares.size();
                        ListaLugares.clear();
                        adapter.notifyItemRangeRemoved(0, size);*/

                        ElemenBusarLugares usuario = null;
                        JSONArray json = response.optJSONArray("usuario");
                        for (int i = 0; i < json.length(); i++) {
                            usuario = new ElemenBusarLugares();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            //       Toast.makeText(getApplicationContext(), jsonObject.optString("Direccion"), Toast.LENGTH_LONG).show();
                            usuario.setDireccion(jsonObject.optString("Direccion"));
                            usuario.setTitulo("");
                            usuario.setDisponibles("");
                            usuario.setNombreLugar(jsonObject.optString("Nombre"));
                            usuario.setPrecio("");
                            usuario.setTipo(jsonObject.optString("Version"));
                            usuario.setOferta("");
                            usuario.setCiudad(jsonObject.optString("Ciudad"));
                            usuario.setEstado(jsonObject.optString("Estado"));
                            usuario.setEslogan(jsonObject.optString("Eslogan"));
                            usuario.setTelefono(jsonObject.optString("Telefono"));
                            usuario.setCorreo(jsonObject.optString("Correo"));
                            usuario.setLatitud(jsonObject.optString("Latitud"));
                            usuario.setLonguitud(jsonObject.optString("Longuitud"));
                            usuario.setBd(jsonObject.optString("bd"));
                            ListaLugares.add(usuario);
                        }
                        rvLugaresFavoritos.setAdapter(adapter);
                        content.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    rvLugaresFavoritos.removeAllViewsInLayout();
                    final int size = ListaLugares.size();
                    ListaLugares.clear();
                    adapter.notifyItemRangeRemoved(0, size);
                    Toast.makeText(getApplicationContext(), "No se encontraron.", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }
        if(sino){
            pg.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No tienes ningun lugar agregado a tu favoritos.", Toast.LENGTH_SHORT).show();
        }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
