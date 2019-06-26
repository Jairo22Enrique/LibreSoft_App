package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorRegistro_Ventas;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.Entidades.ElemenRegistro_Ventas;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class RegistroVentas extends AppCompatActivity {
RecyclerView rvVentas;
    ArrayList<ElemenRegistro_Ventas> listaVentas;
    TextView txtTotalLibros,NoVentas,txtTotalVentas;
    ProgressBar pg;
    AdaptadorRegistro_Ventas adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_ventas);
        listaVentas = new ArrayList<>();
        adapter = new AdaptadorRegistro_Ventas(listaVentas);
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
        toolbar.setTitle("Ventas del día.");
        toolbar.setTitleTextColor(Color.WHITE);
        pg=findViewById(R.id.progress);
        rvVentas = findViewById(R.id.rvVentas);
        rvVentas.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvVentas.setLayoutManager(llm);
        rvVentas.setItemAnimator(new DefaultItemAnimator());
        rvVentas.setVisibility(View.GONE);
        CargarVentas();
        CargarReporte();
        txtTotalVentas=findViewById(R.id.txtTotalVentas);
        txtTotalLibros=findViewById(R.id.txtTotalLibros);
        NoVentas=findViewById(R.id.NoVentas);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setVisibility(View.VISIBLE);
                CargarReporte();
                CargarVentas();
            }
        });
    }

    private void CargarVentas() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
      //  fecha="2018-05-10";
        String lugar = DatosUsuarioLocal.bd;
        //lugar="Libreria_x";

        String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosVendidos.php?bd="+lugar+"&fecha="+fecha;
        Log.i("AQUI",url);
        url = url.replace(" ","%20");
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        ElemenRegistro_Ventas usuario = null;
                        JSONArray json = response.optJSONArray("usuario");
                        //
                        rvVentas.removeAllViewsInLayout();
                        final int size = listaVentas.size();
                        listaVentas.clear();
                        adapter.notifyItemRangeRemoved(0, size);
                        //
                        for (int i = 0; i < json.length(); i++) {
                            usuario = new ElemenRegistro_Ventas();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                            usuario.setTitulo(jsonObject.optString("Titulo"));
                            usuario.setAutor(jsonObject.optString("Autor"));
                            usuario.setFecha_venta(jsonObject.optString("Fecha_venta"));
                            usuario.setHora_venta(jsonObject.optString("Hora_venta"));
                            usuario.setVendedor(jsonObject.optString("Vendedor"));
                            usuario.setDato(jsonObject.optString("Imagen"));
                            usuario.setTotal(jsonObject.optString("Total"));
                            usuario.setVendidos(jsonObject.optString("Vendidos"));
                            listaVentas.add(usuario);

                            rvVentas.setAdapter(adapter);
                        }
                        rvVentas.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"No se encontró",Toast.LENGTH_SHORT).show();
error.printStackTrace();
                    pg.setVisibility(View.GONE);
                }
            });
            queue.add(stringRequest);
        }
    private void CargarReporte() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        String lugar = DatosUsuarioLocal.bd;


        String url = "https://libresoft.000webhostapp.com/volley/ObtenerRegistroVentas.php?bd="+lugar+"&fecha="+fecha;
        Log.i("AQUI",url);
        url = url.replace(" ","%20");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        NoVentas.setText("");
                        txtTotalLibros.setText("");
                        txtTotalVentas.setText("");
                        String a=jsonObject.optString("No_ventas");
                        if(a==null||a.equalsIgnoreCase("null")||a.equals("")){
                            a="";
                            NoVentas.setText("Número de ventas en el día: "+a);
                            txtTotalLibros.setText("Total de Libros vendidos: "+a);
                            txtTotalVentas.setText("Total de ventas en el día: $"+a);
                            return;
                        }
                        NoVentas.setText("Número de ventas en el día: "+jsonObject.optString("No_ventas"));
                        txtTotalLibros.setText("Total de Libros vendidos: "+jsonObject.optString("Libros_vendidos"));
                        txtTotalVentas.setText("Total de ventas en el día: $"+jsonObject.optString("Total"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se encontró",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });
        queue.add(stringRequest);
    }

}
