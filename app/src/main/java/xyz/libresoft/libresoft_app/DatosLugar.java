package xyz.libresoft.libresoft_app;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.RecoverySystem;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
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

import xyz.libresoft.libresoft_app.Adaptadores.Adaptador;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.Mapas.MapaLugarEspecifico;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class DatosLugar extends AppCompatActivity {
    TextView txtEslogan, txtDireccion, txtCiudad, txtEstado, txtTel, txtCorreo;
    ImageView ImagenLugar;
    Spinner BuscarPor,CmbGeneros;
    ImageButton btnVerEnMapa;
    RecyclerView rvLugaresRel;
    ArrayList<Elemen> listaReservas2;
    ProgressBar pg;
    RelativeLayout conten;
    boolean guardado = false;
    SearchView sv;
    TextView txtTexto;
    Adaptador adapter ;
    ArrayList <String> GenerosList=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_lugar);
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
            toolbar.setTitleTextColor(Color.WHITE);
            listaReservas2 = new ArrayList<>();
            adapter = new Adaptador(listaReservas2);
            toolbar.setTitle(getIntent().getStringExtra("NombreLugar"));
            pg = findViewById(R.id.progress);
            txtEslogan = findViewById(R.id.txtEslogan);
            txtEslogan.setText("'" + getIntent().getStringExtra("Eslogan") + "'");
            txtDireccion = findViewById(R.id.txtDireccion);
            txtDireccion.setText("Direccion: " + getIntent().getStringExtra("Direccion"));
            txtCiudad = findViewById(R.id.txtCiudad);
            txtCiudad.setText("Cuidad: " + getIntent().getStringExtra("Cuidad"));
            txtEstado = findViewById(R.id.txtEstado);
            txtEstado.setText("Estado: " + getIntent().getStringExtra("Estado"));
            txtTel = findViewById(R.id.txtTel);
            txtTel.setText("Teléfono: " + getIntent().getStringExtra("Telefono"));
            txtCorreo = findViewById(R.id.txtCorreo);
            txtCorreo.setText("Correo: " + getIntent().getStringExtra("Correo"));
            txtCorreo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String corre=txtCorreo.getText().toString().replace("Correo: ","");
                            String[] TO = {corre}; //aquí pon tu correo
                            String[] CC = {""};
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
// Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reciba un cordial saludo.");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Atte. "+DatosUsuarioLocal.Nombre);

                            try {
                                startActivity(Intent.createChooser(emailIntent, "Enviar email a "+corre));
                                // finish();
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getApplicationContext(),
                                        "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                            }
                        }
            });

            ImagenLugar = findViewById(R.id.ImagenLugar);
           int im=R.drawable.bibliotecas;
            if(getIntent().getStringExtra("Tipo").equals("Libreria")){
im=R.drawable.librerias;
            }
            ImagenLugar.setImageResource(im);

            btnVerEnMapa = findViewById(R.id.btnVerEnMapa);
            rvLugaresRel = findViewById(R.id.rvLugaresRel);
            LinearLayoutManager ll = new LinearLayoutManager(this);
            rvLugaresRel.setHasFixedSize(true);
            rvLugaresRel.setLayoutManager(ll);
            conten = findViewById(R.id.conten);
            conten.setVisibility(View.GONE);
            LibrosRel("https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd="+getIntent().getStringExtra("bd")+"&tipo="+getIntent().getStringExtra("Tipo")+"&valor=&donde=");
            txtTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dial = "tel:" + getIntent().getStringExtra("Telefono");

                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
            });

            btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(getApplicationContext(), MapaLugarEspecifico.class);
                        i.putExtra("Latitud", getIntent().getStringExtra("Latitud"));
                        i.putExtra("Longuitud", getIntent().getStringExtra("Longuitud"));
                        i.putExtra("NombreLugar", getIntent().getStringExtra("NombreLugar"));
                        i.putExtra("Direccion", getIntent().getStringExtra("Direccion"));
                        i.putExtra("Tipo", getIntent().getStringExtra("Tipo"));
                        i.putExtra("Eslogan", getIntent().getStringExtra("Eslogan"));
                        i.putExtra("Telefono", getIntent().getStringExtra("Telefono"));
                        i.putExtra("Correo", getIntent().getStringExtra("Correo"));
                        //   i.putExtra("Precio",getIntent().getStringExtra("Precio"));
                        //    i.putExtra("Disponibles",getIntent().getStringExtra("Disponibles"));
                        //    i.putExtra("Oferta",getIntent().getStringExtra("Oferta"));
                        //      i.putExtra("Titulo",getIntent().getStringExtra("Titulo"));
                        startActivity(i);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Ubicación no disponible",Toast.LENGTH_LONG).show();
                    }
                }
            });
            //fab

            TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            Cursor datos = db.rawQuery(
                    "SELECT * FROM Lugares_Favoritos WHERE codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario + "' and nombre_bd = '" + getIntent().getStringExtra("bd")+"'", null
            );

            while (datos.moveToNext()) {
                guardado = true;
            }

            //
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (guardado) {
                fab.setImageResource(android.R.drawable.star_big_on);
            } else {
                fab.setImageResource(android.R.drawable.star_big_off);
            }


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (guardado) {
                        //delete
                        TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
                        SQLiteDatabase db = cnx.getWritableDatabase();
                        db.delete("Lugares_Favoritos", "codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario + "' and nombre_bd = '" + getIntent().getStringExtra("bd")+"'", null);
                        fab.setImageResource(android.R.drawable.star_big_off);
                        Toast.makeText(getApplicationContext(), "Se ha eliminado de tus favoritos.", Toast.LENGTH_LONG).show();
                        return;
                    }


                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    ContentValues datos = new ContentValues();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String fecha = dateFormat.format(date);
                    datos.put("codigo_usuario", DatosUsuarioLocal.Codigo_Usuario);
                    datos.put("fecha", fecha);
                    datos.put("nombre_bd", getIntent().getStringExtra("bd"));
                    db.insert("Lugares_Favoritos", null, datos);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Se ha agregado tus lugares favoritos", Toast.LENGTH_LONG).show();
                    fab.setImageResource(android.R.drawable.star_big_on);

                }
            });
            //----------
            CargarGeneros();
            CmbGeneros = findViewById(R.id.CmbGeneros);



            sv = findViewById(R.id.searchview);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String donde = BuscarPor.getSelectedItem().toString(), valor = sv.getQuery().toString();

                    if (BuscarPor.getSelectedItem().toString().equalsIgnoreCase("Genero")) {
                        valor = CmbGeneros.getSelectedItem().toString();
                    }
                    if (valor.equals("")) {
                        Toast.makeText(getApplicationContext(), "Proporciona un término para buscar", Toast.LENGTH_SHORT).show();

                    } else {

                        String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd=" +getIntent().getStringExtra("bd") + "&tipo=Libreria&valor=" + valor + "&donde=" + donde;
                        pg.setVisibility(View.VISIBLE);
                        conten.setVisibility(View.GONE);
                       LibrosRel(url);
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            BuscarPor = findViewById(R.id.BuscarPor);
            String[] opciones1 = {"Titulo", "Autor", "Editorial", "Genero"};
            ArrayAdapter<String> adaptacion1 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, opciones1);
            BuscarPor.setAdapter(adaptacion1);
            BuscarPor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Genero")) {
                        sv.setEnabled(false);
                        sv.setQuery("", false);
                        CmbGeneros.setVisibility(View.VISIBLE);
                    } else {
                        CmbGeneros.setVisibility(View.INVISIBLE);
                        sv.setEnabled(true);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            CmbGeneros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (sv.isEnabled()) {
                        return;
                    }
                    sv.setQuery(parent.getItemAtPosition(position).toString(), false);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            txtTexto=findViewById(R.id.txtTexto);
            txtTexto.setText("Resultados para: Todo.\nBuscando por general");


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void LibrosRel(String url){

        url = url.replace(" ", "%20");
        Log.i("AQUI",url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //---
                    rvLugaresRel.removeAllViewsInLayout();
                    final int size = listaReservas2.size();
                    listaReservas2.clear();
                    adapter.notifyItemRangeRemoved(0, size);
                    //-------------
                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new Elemen();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        if(jsonObject.optString("Titulo").equals(getIntent().getStringExtra("Titulo"))){
                            continue;
                        }
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
                        usuario.setUnidades(jsonObject.optString("Unidades"));
                        usuario.setEstante(jsonObject.optString("Estante"));
                        usuario.setBuscandoEn(getIntent().getStringExtra("NombreLugar"));
                        listaReservas2.add(usuario);

                        rvLugaresRel.setAdapter(adapter);
                    }
                    pg.setVisibility(View.GONE);
                    conten.setVisibility(View.VISIBLE);
                    if(listaReservas2.size()==0){
                        usuario.setTitulo("No se han encontrado libros relacionados.");
                        listaReservas2.add(usuario);
                        Adaptador adapter = new Adaptador(listaReservas2);
                        rvLugaresRel.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Nada que mostrar.",Toast.LENGTH_LONG).show();
                pg.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
    }
    private void CargarGeneros() {
        //

        String url="https://libresoft.000webhostapp.com/volley/ObtenerGenerosLugar.php?bd="+getIntent().getStringExtra("bd");
        url = url.replace(" ", "%20");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
//---------------------

//---------------------
                    for (int i = 0; i < json.length(); i++) {


                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        GenerosList.add(jsonObject.optString("Genero"));

                        Log.i("GENERO",jsonObject.optString("Genero"));

                        //  Adaptador adapter = new Adaptador(listaReservas2);

                    }

                    ArrayAdapter<String> adaptaciongen = new ArrayAdapter<String>(getApplication(), R.layout.simple_spinner_propio, GenerosList);
                    CmbGeneros.setAdapter(adaptaciongen);
                    CmbGeneros.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se encontró",Toast.LENGTH_SHORT).show();


            }
        });
        queue.add(stringRequest);
        //

    }
}
