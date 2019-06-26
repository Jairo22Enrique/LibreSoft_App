package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class ConsultarLibros extends AppCompatActivity {
    Spinner BuscarPor,CmbGeneros;
    SearchView sv;
    RecyclerView rvLibros;
    ArrayList<Elemen> listaReservas2;
    ProgressDialog dialog;
    Adaptador adapter = null;
    TextView txtTexto;
    Button btnregis;
    ArrayList <String> GenerosList=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_consultar_libros);
            listaReservas2 = new ArrayList<>();


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
            toolbar.setTitle(DatosUsuarioLocal.bd.replace("_", " "));
            toolbar.setTitleTextColor(Color.WHITE);
             adapter = new Adaptador(listaReservas2);
//recycler
            rvLibros = findViewById(R.id.rvLibros);
            rvLibros.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvLibros.setLayoutManager(llm);
            rvLibros.setItemAnimator(new DefaultItemAnimator());
            //
            String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd=" + DatosUsuarioLocal.bd + "&tipo=Libreria&valor=*&donde=";
            url = url.replace(" ", "%20");
            Enviar(url);
            CmbGeneros = findViewById(R.id.CmbGeneros);
            CargarGeneros();
/*Thread.sleep(1000);
            String[] Generos = {"Clásico",
                    "Didáctico",
                    "Juvenil",
                    "Ciencia ficción",
                    "Fantasia",
                    "Guía",
                    "Romance",
                    "Científicos",
                    "Terror",
                    "Suspenso",
                    "Superación",
                    "Aventuras",
                    "Código y leyes",
                    "Novelas",
                    "Tragedia",
                    "Realismo Magico",
                    "Misterio",
                    "Historia",
                    "Biografias",
                    "Comedia y otros"
            };

            ArrayAdapter<String> adaptaciongen = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, GenerosList);
            CmbGeneros.setAdapter(adaptaciongen);
            CmbGeneros.setVisibility(View.INVISIBLE);*/
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
                        String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd=" + DatosUsuarioLocal.bd + "&tipo=Libreria&valor=" + valor + "&donde=" + donde;
                        url = url.replace(" ", "%20");
                        Enviar(url);
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

                btnregis = findViewById(R.id.btnregis);
                btnregis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (btnregis.getText().toString().equalsIgnoreCase("Ver datos.")) {

                                String url = "http://libresoft.000webhostapp.com/volley/ObtenerUsuarioBiblioteca.php?bd=" + DatosUsuarioLocal.bd + "&codigo=" + DatosUsuarioLocal.Codigo_Usuario;
                                url = url.replace(" ", "%20");
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String Nombre="",Turno="",Grado="",Codigo="",Grupo="",No_control="",Edad="",Tel="";
                                            JSONArray json = response.optJSONArray("usuario");
                                            for (int i = 0; i < json.length(); i++) {
                                                JSONObject jsonObject = null;
                                                jsonObject = json.getJSONObject(i);
                                                 Nombre = (jsonObject.optString("Nombre"));
                                                 Turno = (jsonObject.optString("Turno"));
                                                 Grado = (jsonObject.optString("Grado"));
                                                 Grupo = (jsonObject.optString("Grupo"));
                                                 Tel = (jsonObject.optString("Tel"));
                                                 Codigo = (jsonObject.optString("Codigo"));
                                                 No_control = (jsonObject.optString("No_Control"));
                                                 Edad = (jsonObject.optString("Edad"));

                                            }

                                            String mess="No. Control: " + No_control + "\nGrado: " + Grado + "\nGrupo: " + Grupo + "\nTurno: "+Turno+"\nTelefono: " + Tel + "\nEdad: " + Edad + "\nCODIGO: " + Codigo;
MostratDatos(Nombre,mess);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Nada que mostrar", Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                });
                                queue.add(stringRequest);

                            }else {
                                Intent i = new Intent(getApplicationContext(), Registrarse_Biblio.class);
                                startActivity(i);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Dueño de libreria")) {
            btnregis.setVisibility(View.GONE);
            }
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd=" + DatosUsuarioLocal.bd + "&tipo=Libreria&valor=*&donde=";
                        url = url.replace(" ", "%20");
                        Enviar(url);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Algo ha pasado.\nIntente más tarde", Toast.LENGTH_LONG).show();
                    }
                }
            });

            verificarUsuario();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void CargarGeneros() {
    //
        String url="https://libresoft.000webhostapp.com/volley/ObtenerGenerosLugar.php?bd="+DatosUsuarioLocal.bd;
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
                    dialog.hide();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se encontró",Toast.LENGTH_SHORT).show();
                dialog.hide();

            }
        });
        queue.add(stringRequest);
        //

    }

    private void verificarUsuario() {
        try{
    String url="http://libresoft.000webhostapp.com/volley/ObtenerUsuarioBibliotecaSiExite.php?bd="+DatosUsuarioLocal.bd+"&codigo="+DatosUsuarioLocal.Codigo_Usuario;
    url=url.replace(" ","%20");
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Exite")) {
                        btnregis.setText("Ver datos.");
                    }
                    //dialog.hide();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //dialog.hide();
                    Toast.makeText(getApplicationContext(), "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    void Enviar(String url){
dialog =new ProgressDialog(this);
dialog.setMessage("Buscando...");
dialog.setIcon(R.drawable.ic_action_reload);
dialog.show();
        Log.i("AQUI",url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
//---------------------
                    rvLibros.removeAllViewsInLayout();
                    final int size = listaReservas2.size();
                    listaReservas2.clear();
                    adapter.notifyItemRangeRemoved(0, size);
//---------------------

                    for (int i = 0; i < json.length(); i++) {
                        usuario = new Elemen();
                     /*   if(listaReservas2.size()>9){
                            continue;
                        }*/
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
                        //
                        usuario.setUnidades(jsonObject.optString("Unidades"));
                        usuario.setEstante(jsonObject.optString("Estante"));
                        usuario.setBuscandoEn(DatosUsuarioLocal.bd);
                        Log.i("NOMBRE",jsonObject.optString("Nombre"));
                        listaReservas2.add(usuario);
                      //  Adaptador adapter = new Adaptador(listaReservas2);
                        rvLibros.setAdapter(adapter);
                    }
                    if(listaReservas2.size()==0){
                        usuario.setTitulo("No se han encontrado libros relacionados.");
                        listaReservas2.add(usuario);
                    //     adapter = new Adaptador(listaReservas2);
                        rvLibros.setAdapter(adapter);
                    }
                    if(!sv.getQuery().equals("")) {
                        txtTexto.setText("Resultados para: " + sv.getQuery() + ".\nBuscando por " + BuscarPor.getSelectedItem().toString() + " --> Encontrados: " + listaReservas2.size());
                    }
                    dialog.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se encontró",Toast.LENGTH_SHORT).show();
                dialog.hide();

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    void MostratDatos(String titulo,String mess){
        final CharSequence[] opciones={"Ok"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mess);
        builder.setIcon(R.drawable.ic_action_user);
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}
