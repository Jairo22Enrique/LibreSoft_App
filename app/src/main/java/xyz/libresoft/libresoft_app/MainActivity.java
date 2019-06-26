package xyz.libresoft.libresoft_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;

import xyz.libresoft.libresoft_app.Adaptadores.Adaptador;
import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorRecomendadosPrincipal;
import xyz.libresoft.libresoft_app.Clases.Conexion;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.Lector.CamaraLector;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosRegistrado;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Spinner BuscarPor,CmbGeneros,Estado,Ciudad;
    SearchView sv;
    RadioButton RdPrecio,RdCalificacion,RdAlfabeto;
    RecyclerView rvMejoresCal,rvRecomendados,rvRecientes;
    LinearLayout filtros;
    ArrayList<Elemen> listaReservas2,listaMejores,listaRecientes;
    ProgressBar pg1,pg2,pg3;
    RelativeLayout conten1,conten2,conten3;
    int fallos=0,sise=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if(!Conexion.isConnected(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"No hay internet para usar la app",Toast.LENGTH_SHORT).show();
                return;
            }
            //Recycler
            listaReservas2 = new ArrayList<>();
            listaMejores = new ArrayList<>();
            listaRecientes = new ArrayList<>();
            rvMejoresCal =findViewById(R.id.rvMejoresCal);
            rvMejoresCal.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            rvMejoresCal.setLayoutManager(llm);
            rvRecomendados =findViewById(R.id.rvRecomendados);
            rvRecomendados.setHasFixedSize(true);
            LinearLayoutManager llm1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            rvRecomendados.setLayoutManager(llm1);
            CargarRecomendados();
//--Cargar datos
            CargarMejoresCal();
            rvRecientes =findViewById(R.id.rvRecientes);
            rvRecientes.setHasFixedSize(true);
            LinearLayoutManager llm2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            rvRecientes.setLayoutManager(llm2);
            CargarRecientes();


//
            pg1=findViewById(R.id.progress1);
            pg2=findViewById(R.id.progress2);
            pg3=findViewById(R.id.progress3);
            conten1=findViewById(R.id.conten1);
            conten2=findViewById(R.id.conten2);
            conten3=findViewById(R.id.conten3);
            conten1.setVisibility(View.GONE);
            conten2.setVisibility(View.GONE);
            conten3.setVisibility(View.GONE);
            //
            filtros=findViewById(R.id.Filtros);
            filtros.setVisibility(View.GONE);
            Estado = findViewById(R.id.Estado);
            Ciudad = findViewById(R.id.Ciudad);
            String[] Estados={"Michoacan","Yucatan"};
            ArrayAdapter<String> adaptacion2 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio,Estados);
            Estado.setAdapter(adaptacion2);

            final String[] Cuidades={"Zitacuaro","Merida"};
            ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio,Cuidades);
            Ciudad.setAdapter(adaptacion3);
            Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(Estado.getSelectedItem().toString().equalsIgnoreCase("Michoacan")){
                        String[]  Cuidades={"Zitacuaro"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_propio,Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                    }
                    else if(Estado.getSelectedItem().toString().equalsIgnoreCase("Yucatan")){
                        String[]  Cuidades={"Merida"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_propio,Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            CmbGeneros=findViewById(R.id.CmbGeneros);
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
                    "Comedia y otros"};
            ArrayAdapter<String> adaptaciongen = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, Generos);
            CmbGeneros.setAdapter(adaptaciongen);
            CmbGeneros.setVisibility(View.INVISIBLE);
            RdAlfabeto=findViewById(R.id.RdAlfabeto);
            RdCalificacion=findViewById(R.id.RdCalificacion);
            RdCalificacion.setChecked(true);
            RdPrecio=findViewById(R.id.RdPrecio);

            sv=findViewById(R.id.searchview);
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent i = new Intent(getApplicationContext(), LibrosPrincipal.class);
                    String donde = BuscarPor.getSelectedItem().toString(),valor =sv.getQuery().toString();

                    if(BuscarPor.getSelectedItem().toString().equalsIgnoreCase("Genero")){
                        valor=CmbGeneros.getSelectedItem().toString();
                    }
                    if(valor.equals("")){
                        Toast.makeText(getApplicationContext(),"Proporciona un término para buscar", Toast.LENGTH_SHORT).show();

                    }else{
                        String order="calificacion";
                        if(RdAlfabeto.isChecked()){
                            order="titulo";
                        }if(RdCalificacion.isChecked()){
                            order="calificacion";
                        }
                        if(RdPrecio.isChecked()){
                            order="Precio";
                        }
                        if(filtros.getVisibility()==View.VISIBLE){
                            DatosUsuarioLocal.EstadoParaBuscar=Estado.getSelectedItem().toString();
                            DatosUsuarioLocal.CiudadParaBuscar=Ciudad.getSelectedItem().toString();
                        }else{
                            DatosUsuarioLocal.EstadoParaBuscar=DatosUsuarioLocal.Estado;
                            DatosUsuarioLocal.CiudadParaBuscar=DatosUsuarioLocal.Ciudad;
                        }
                    String url="https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor="+valor+"&donde="+donde+"&tipo=general&order="+order;
                    url=url.replace(" ","%20");
                    i.putExtra("url",url);
                    i.putExtra("donde",donde);
                    i.putExtra("valor",valor);
                    startActivity(i);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            BuscarPor=findViewById(R.id.BuscarPor);
            String[] opciones1 = {"Titulo","Autor","Editorial","Genero"};
            ArrayAdapter<String> adaptacion1 = new ArrayAdapter<String>(this, R.layout.simple_spinner_propio, opciones1);
            BuscarPor.setAdapter(adaptacion1);
            BuscarPor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getItemAtPosition(position).toString().equalsIgnoreCase("Genero")){
                        sv.setEnabled(false);
                        sv.setQuery("",false);
                        CmbGeneros.setVisibility(View.VISIBLE);
                    }else{
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
                    if(sv.isEnabled()){
                        return;
                    }
                    sv.setQuery(parent.getItemAtPosition(position).toString(),false);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setImageResource(R.drawable.filter);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(filtros.getVisibility()==View.GONE){
                       filtros.setVisibility(View.VISIBLE);
                   }else{
                       filtros.setVisibility(View.GONE);
                   }
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);



          //  Toast.makeText(this, DatosUsuarioLocal.bd+DatosUsuarioLocal.Tipo,Toast.LENGTH_SHORT).show();

            if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Usuario")) {
                navigationView.getMenu().setGroupVisible(R.id.grupoCUIL, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoDueños, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoDueñosLibreria, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoCuilbiblioteca, false);
            }
            else if((DatosUsuarioLocal.Tipo.equalsIgnoreCase("Maestro")||DatosUsuarioLocal.Tipo.equalsIgnoreCase("Estudiante"))&&!DatosUsuarioLocal.CUIL.equalsIgnoreCase("null")) {
                navigationView.getMenu().setGroupVisible(R.id.grupoDueños, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoDueñosLibreria, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoCUIL, false);
            }
            else if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Bibliotecario")){
                navigationView.getMenu().setGroupVisible(R.id.grupoDueñosLibreria, false);
                navigationView.getMenu().setGroupVisible(R.id.grupoCUIL, false);
            }
            else if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Dueño de Libreria")){

                navigationView.getMenu().setGroupVisible(R.id.grupoCuilbiblioteca, false);
            }


        }catch (Exception e){
        //    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        InstSpinner();
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

    private void CargarMejoresCal() {

        String genero="*";
        String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + genero + "&donde=&tipo=general&order=calificacion";
        url = url.replace(" ", "%20");
        Log.i("AQUI",url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new Elemen();
                        if(listaMejores.size()>9){
                            continue;
                        }
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
                        usuario.setUnidades("");
                        usuario.setEstante("");
                        usuario.setBuscandoEn("");

                        listaMejores.add(usuario);
                        AdaptadorRecomendadosPrincipal adapter = new AdaptadorRecomendadosPrincipal(listaMejores);
                        rvMejoresCal.setAdapter(adapter);


                    }
                    conten2.setVisibility(View.VISIBLE);
                    pg2.setVisibility(View.GONE);
                    fallos=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                //    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error al conectar",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

                if(fallos==3){
                    Toast.makeText(getApplicationContext(),"El servidor no ha respondido despues de 3 intentos",Toast.LENGTH_SHORT).show();
                    return;
                }
                fallos++;
                CargarMejoresCal();
            }
        });
        queue.add(stringRequest);

    }

    private void CargarRecomendados() {
        try {
            TablaUsuariosDatos cnx = new TablaUsuariosDatos(this, "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            Cursor datos = db.rawQuery(
                    "select titulo,genero,count(genero) as count from Consultados WHERE codigo_usuario = '"+DatosUsuarioLocal.Codigo_Usuario+"'group by genero order by count desc limit 1", null
            );
            String genero = "";
            while (datos.moveToNext()) {
                genero = datos.getString(1);
                Log.i("AQUI", datos.getString(1));
            }
            if(genero.equals("")||genero.isEmpty()){
                genero="Novelas";
            }
            db.close();
            String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + genero + "&donde=genero&tipo=general&order=calificacion";
            //     String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + getIntent().getStringExtra     ("Genero") + "&donde=genero&tipo=general&order=calificacion";
            url = url.replace(" ", "%20");
            Log.i("AQUI", url);
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Elemen usuario = null;
                        JSONArray json = response.optJSONArray("usuario");
                        for (int i = 0; i < json.length(); i++) {
                            Log.i("AQUI SIZE",json.length()+"");
                            sise++;
                            if(sise>json.length()){
                                continue;
                            }
                            if (listaReservas2.size() > 9) {
                                continue;
                            }
                            usuario = new Elemen();
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
                            usuario.setUnidades("");
                            usuario.setEstante("");
                            usuario.setBuscandoEn("");

                            listaReservas2.add(usuario);

                        }
                        AdaptadorRecomendadosPrincipal adapter = new AdaptadorRecomendadosPrincipal(listaReservas2);
                        rvRecomendados.setAdapter(adapter);
                        fallos = 0;
                        conten1.setVisibility(View.VISIBLE);
                        pg1.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
            //        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();

                    if (fallos == 3) {
                        Toast.makeText(getApplicationContext(), "El servidor no ha respondido despues de 3 intentos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fallos++;
                    if(listaReservas2.size()!=0){
                        return;
                    }

                    CargarRecomendados();
                }
            });
            queue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
         //   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void CargarRecientes() {
        String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=*&donde=codigo&tipo=general&order=codigo";
        //     String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + getIntent().getStringExtra     ("Genero") + "&donde=genero&tipo=general&order=calificacion";
        url = url.replace(" ", "%20");
        Log.i("AQUI",url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new Elemen();
                        if(listaRecientes.size()>9){
                            continue;
                        }
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
                        usuario.setUnidades("");
                        usuario.setEstante("");
                        usuario.setBuscandoEn("");

                        listaRecientes.add(usuario);
                        AdaptadorRecomendadosPrincipal adapter = new AdaptadorRecomendadosPrincipal(listaRecientes);
                        rvRecientes.setAdapter(adapter);


                    }
                    fallos=0;
                    conten3.setVisibility(View.VISIBLE);
                    pg3.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
            //    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                error.printStackTrace();

                if(fallos==3){
                    Toast.makeText(getApplicationContext(),"El servidor no ha respondido despues de 3 intentos",Toast.LENGTH_SHORT).show();
                    return;
                }
                fallos++;
                CargarRecientes();
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.close) {
            //System.exit(0);
            TablaUsuariosRegistrado cnx = new TablaUsuariosRegistrado(this,"UsuarioRegistrado",null,1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            db.delete("Usuario","",null);
            db.close();
            finish();
            Intent i = new Intent(getApplicationContext(), IngresarDos.class);
            startActivity(i);
        }
        else if(id==R.id.contac){
            Intent i=new Intent(this,Contacto.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.lugares) {
            Intent i=new Intent(this,BLibreriaBiblioteca.class);
            startActivity(i);
        } else if (id == R.id.nav_LibrosGuardados) {
            Intent i=new Intent(this,LibrosGuardados.class);
            startActivity(i);
        } else if (id == R.id.nav_LugaresFavoritos) {
            Intent i=new Intent(this,LugaresFavoritos.class);
            startActivity(i);
        } else if (id == R.id.nav_consultar) {
            Intent i=new Intent(this,ConsultarLibros.class);
            startActivity(i);
        } else if (id == R.id.nav_mibiblioteca) {
            Intent i=new Intent(this,ConsultarLibros.class);
            startActivity(i);
        } else if (id == R.id.nav_scanner) {
            Intent i=new Intent(this,CamaraLector.class);
            startActivity(i);
        } else if (id == R.id.nav_ventas) {
            Intent i=new Intent(this,RegistroVentas.class);
            startActivity(i);
        } else if (id == R.id.nav_datos) {
            Intent i=new Intent(this,Datos.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargarMejoresCal();
        CargarRecomendados();
        CargarRecientes();
    }
}
