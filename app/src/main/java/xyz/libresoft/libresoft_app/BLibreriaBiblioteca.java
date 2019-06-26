package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.SearchView;
import android.widget.Spinner;
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

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorBuscarLugares;
import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorLugaresGeneral;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.Mapas.MapasLugaresGeneral;
import xyz.libresoft.libresoft_app.Mapas.MapasLugaresGeneralGeneral;

public class BLibreriaBiblioteca extends AppCompatActivity {

    Spinner seleccion,Ciudad,Estado;
    ImageButton btnMapa;
    RecyclerView rvLugares;
    SearchView sv;
    ArrayList<ElemenBusarLugares> ListaLugares;
    AdaptadorLugaresGeneral adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blibreria_biblioteca);
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
            toolbar.setTitle("Buscar");
            ListaLugares = new ArrayList<>();
            adapter = new AdaptadorLugaresGeneral(ListaLugares);
            rvLugares = findViewById(R.id.rvLugares);
            rvLugares.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvLugares.setLayoutManager(llm);
            seleccion = findViewById(R.id.seleccion);
            Ciudad = findViewById(R.id.Ciudad);
            Estado = findViewById(R.id.Estado);
            String[] Estados = {"Michoacan", "Yucatan"};
            ArrayAdapter<String> adaptacion2 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, Estados);
            Estado.setAdapter(adaptacion2);

            final String[] Cuidades = {"Zitacuaro", "Merida"};
            ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, Cuidades);
            Ciudad.setAdapter(adaptacion3);
            Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (Estado.getSelectedItem().toString().equalsIgnoreCase("Michoacan")) {
                        String[] Cuidades = {"Zitacuaro"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_propio, Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                    } else if (Estado.getSelectedItem().toString().equalsIgnoreCase("Yucatan")) {
                        String[] Cuidades = {"Merida"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_propio, Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            String[] opciones = {"Ambas", "Libreria", "Biblioteca"};
            ArrayAdapter<String> adaptacion1 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, opciones);
            seleccion.setAdapter(adaptacion1);
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
            String ver = seleccion.getSelectedItem().toString();
            if(ver.equalsIgnoreCase("Ambas")){
                ver="";
            }
            String url="http://libresoft.000webhostapp.com/volley/ObtenerLugares.php?estado="+ DatosUsuarioLocal.Estado+"&ciudad="+DatosUsuarioLocal.Ciudad+"&version="+ver;
            url = url.replace(" ","%20");
            Lugares(url);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ver = seleccion.getSelectedItem().toString();
                    if(ver.equalsIgnoreCase("Ambas")){
                        ver="";
                    }
                    String url="http://libresoft.000webhostapp.com/volley/ObtenerLugares.php?estado="+Estado.getSelectedItem().toString()+"&ciudad="+Ciudad.getSelectedItem().toString()+"&version="+ver;
                    url = url.replace(" ","%20");
                    Lugares(url);
                }
            });
            sv = findViewById(R.id.searchview);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    String nome=sv.getQuery().toString();
                    if(nome.equals("")){
                        Toast.makeText(getApplicationContext(),"No dejes campos en blanco",Toast.LENGTH_SHORT).show();

                    }else {
                        String url = "https://libresoft.000webhostapp.com/volley/ObtenerLugarNombreGeneral.php?nombre=" + nome+"&estado=" + Estado.getSelectedItem().toString() + "&ciudad=" + Ciudad.getSelectedItem().toString();
                        url=url.replace(" ","%20");
                        Lugares(url);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            InstSpinner();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void InstSpinner() {
        for (int i = 0; i < Estado.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            int posicion=0;
            if (Estado.getItemAtPosition(i).toString().equalsIgnoreCase(DatosUsuarioLocal.Estado)) {
                posicion = i;
            }
            Estado.setSelection(posicion);
            if (Ciudad.getItemAtPosition(i).toString().equalsIgnoreCase(DatosUsuarioLocal.Ciudad)) {
                posicion = i;
            }
            Ciudad.setSelection(posicion);
        }
    }

    private void Lugares(String url) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Consultando...");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    rvLugares.removeAllViewsInLayout();
                    final int size = ListaLugares.size();
                    ListaLugares.clear();
                    adapter.notifyItemRangeRemoved(0, size);

                    ElemenBusarLugares usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new ElemenBusarLugares();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        if(jsonObject.optString("Latitud").equalsIgnoreCase("")||
                                jsonObject.optString("Latitud")==null||
                                jsonObject.optString("Latitud").equalsIgnoreCase("null")){
                            continue;
                        }
                        if(jsonObject.optString("Version").equalsIgnoreCase("BibliotecaE")){
                            continue;
                        }
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

                        rvLugares.setAdapter(adapter);
                    }
                    dialog.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
dialog.hide();
                rvLugares.removeAllViewsInLayout();
                final int size = ListaLugares.size();
                ListaLugares.clear();
                adapter.notifyItemRangeRemoved(0, size);
                Toast.makeText(getApplicationContext(),"No se encontraron.",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
