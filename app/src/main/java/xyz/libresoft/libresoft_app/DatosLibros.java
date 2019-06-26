package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorBuscarLugares;
import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorComentarios;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.Entidades.ElemenComentarios;
import xyz.libresoft.libresoft_app.Mapas.MapasLugaresGeneral;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

public class DatosLibros extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener, View.OnClickListener {

    TextView txtAutor,txtTitulo,txtGenero,txtDescripcion,txtPrecio,txtEditorial,txtAno,txtEdicion,
            txtTituloReseña,txtReseña,txtRating,txtEstante,txtUnidades,disponibleen,tvcomentarios;
    RatingBar CalificacionReseña;
    ImageView imagen;
    RecyclerView rv,rvRel,rvComentarios;
    int fallos=0,uso=0;
    ImageButton btnVerMapa;
    Button btnEnviarReseña;
    String url="";
    ProgressDialog dialog;
    LinearLayout contenedor;
    AdaptadorComentarios adapter ;
    ArrayList<ElemenBusarLugares> listaReservas1;
    ArrayList<Elemen> listaReservas2;
    ArrayList<ElemenComentarios> listaComentarios;
    boolean guardado=false;
    RelativeLayout rlComen;
 //ArrayList<Elemen> listaReservas1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_libros);
        getSupportActionBar().hide();
rlComen=findViewById(R.id.rlComen);
            listaReservas1 = new ArrayList<>();
            listaReservas2 = new ArrayList<>();
            listaComentarios = new ArrayList<>();
            adapter = new AdaptadorComentarios(listaComentarios);
            //Extras
            contenedor=findViewById(R.id.ContenExtras);
            txtEstante=findViewById(R.id.txtEstante);
            txtUnidades=findViewById(R.id.txtUnidades);
            if(getIntent().getStringExtra("Estante").equalsIgnoreCase("")){
                txtEstante.setVisibility(View.INVISIBLE);
            //    txtEstante.setText("--->");
            }else{
                contenedor.setVisibility(View.VISIBLE);
                txtEstante.setText("Ubicación: "+getIntent().getStringExtra("Estante"));
            }if(getIntent().getStringExtra("Unidades").equalsIgnoreCase("")){
                txtUnidades.setVisibility(View.INVISIBLE);
            }else{
                contenedor.setVisibility(View.VISIBLE);
                txtUnidades.setText("Unidades: "+getIntent().getStringExtra("Unidades"));
            }
            //
            disponibleen=findViewById(R.id.disponibleen);
            tvcomentarios=findViewById(R.id.comentarios);
        txtAutor=findViewById(R.id.txtAutor);
            txtAno=findViewById(R.id.txtAno);
            txtRating=findViewById(R.id.txtRating);
            txtRating.setText(getIntent().getStringExtra("Estrellas")+" / 5");
            txtEdicion=findViewById(R.id.txtEdicion);
        txtTitulo=findViewById(R.id.txtTitulo);
        txtGenero=findViewById(R.id.txtGenero);
            txtTituloReseña=findViewById(R.id.txtTituloReseña);
            txtReseña=findViewById(R.id.txtReseña);
            CalificacionReseña=findViewById(R.id.CalificacionReseña);
        btnEnviarReseña=findViewById(R.id.btnEnviarReseña);
            btnEnviarReseña.setOnClickListener(this);

            btnVerMapa=findViewById(R.id.btnVerMapa);
            btnVerMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("TAMAÑO LISTA", listaReservas1.size() + "");
                        if(listaReservas1.size()==0){
                            Toast.makeText(getApplicationContext(),"No hay lugares para mostrar",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] Nombre = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Nombre[i] = listaReservas1.get(i).getNombreLugar();
                        }
                        String[] Direccion = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Direccion[i] = listaReservas1.get(i).getDireccion();
                        }
                        String[] Latitud = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Latitud[i] = listaReservas1.get(i).getLatitud();
                        }
                        String[] Longuitud = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Longuitud[i] = listaReservas1.get(i).getLonguitud();
                        }
                        String[] Version = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Version[i] = listaReservas1.get(i).getTipo();
                        }
                        String[] Disponibles = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Disponibles[i] = listaReservas1.get(i).getDisponibles();
                        }
                        String[] Tipo = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Tipo[i] = listaReservas1.get(i).getTipo();
                        }
                        String[] Precio = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Precio[i] = listaReservas1.get(i).getPrecio();
                        }
                        String[] Oferta = new String[listaReservas1.size()];
                        for (int i = 0; i < listaReservas1.size(); i++) {
                            Oferta[i] = listaReservas1.get(i).getOferta();
                        }

                        Intent i = new Intent(getApplicationContext(), MapasLugaresGeneral.class);
                        i.putExtra("Nombre", Nombre);
                        i.putExtra("Direccion", Direccion);
                        i.putExtra("Latitud", Latitud);
                        i.putExtra("Longuitud", Longuitud);
                        i.putExtra("Version", Version);
                        i.putExtra("Disponibles", Disponibles);
                        i.putExtra("Tipo", Tipo);
                        i.putExtra("Precio", Precio);
                        i.putExtra("Oferta", Oferta);
                        startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"No hay lugares para mostrar",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        imagen=findViewById(R.id.Imagen);
        txtAutor.setText(getIntent().getStringExtra("Autor"));
        txtTitulo.setText(getIntent().getStringExtra("Titulo"));
        txtGenero.setText(getIntent().getStringExtra("Genero"));
        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("Titulo"));

       try {
            byte[] byteCode= Base64.decode(getIntent().getStringExtra("Imagen"), Base64.DEFAULT);
            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles
            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            Bitmap imagenBit= Bitmap.createScaledBitmap(foto,alto,ancho,true);
            imagen.setImageBitmap(imagenBit);
       }catch (Exception e){
           e.printStackTrace();
           imagen.setImageResource(R.drawable.bookicon);
       }

       txtPrecio=findViewById(R.id.txtPrecio);
        txtEditorial=findViewById(R.id.txtEditorial);
        txtDescripcion=findViewById(R.id.txtDescripcion);
        if(getIntent().getStringExtra("BuscandoEn").equals("")) {
            txtDescripcion.setText("Sinopsis:\n\n" + getIntent().getStringExtra("Descripcion"));
        }else{
            txtDescripcion.setText("Buscando en: "+getIntent().getStringExtra("BuscandoEn").replace("_"," ")+"\n\nSinopsis:\n\n" + getIntent().getStringExtra("Descripcion"));
        }
        txtPrecio.setText("Precio promedio: $"+getIntent().getStringExtra("Precio"));
        txtEditorial.setText("Editorial: "+getIntent().getStringExtra("Editorial"));
            txtAno.setText("Año: "+getIntent().getStringExtra("Año"));
            txtEdicion.setText("Edición: "+getIntent().getStringExtra("Edicion"));
            rv = (RecyclerView) findViewById(R.id.rvE);
            rv.setHasFixedSize(true);
        //   LinearLayoutManager llm = new LinearLayoutManager(this);
            LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            //y esto en el xml
            //android:scrollbars="horizontal"
            rv.setLayoutManager(llm);
            rvRel = (RecyclerView) findViewById(R.id.rvRelacionados);
            rvRel.setHasFixedSize(false);
            //   LinearLayoutManager llm = new LinearLayoutManager(this);
            LinearLayoutManager llmR = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            rvRel.setLayoutManager(llmR);
            rvComentarios= (RecyclerView) findViewById(R.id.rvComentarios);
            LinearLayoutManager llmComen = new LinearLayoutManager(this);
            rvComentarios.setHasFixedSize(true);
            rvComentarios.setLayoutManager(llmComen);
            url = "https://libresoft.000webhostapp.com/volley/BuscarLibrosEnLugares.php?titulo="+getIntent().getStringExtra("Titulo")+"&autor="+getIntent().getStringExtra("Autor")+"&editorial="+getIntent().getStringExtra("Editorial");
            url=url.replace(" ","%20");
            uso=1;
         //   EnviarRecibirDatos(url);
            BuscarLugares();
            RecibirComentarios();
            LibrosRelacionado();

            //fab
           // TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
            //SQLiteDatabase db = cnx.getWritableDatabase();
            //guardar la consulta
            TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
            SQLiteDatabase db = cnx.getWritableDatabase();
            ContentValues datos1 = new ContentValues();
            datos1.put("codigo_usuario", DatosUsuarioLocal.Codigo_Usuario);
            datos1.put("genero", getIntent().getStringExtra("Genero"));
            datos1.put("titulo", getIntent().getStringExtra("Titulo"));
            db.insert("Consultados", null, datos1);

            //
            Cursor datos = db.rawQuery(
                    "SELECT * FROM Guardados WHERE codigo_usuario = "+DatosUsuarioLocal.Codigo_Usuario+" and codigo_libro = "+getIntent().getStringExtra("Codigo"), null
            );

            while (datos.moveToNext()) {
               guardado=true;
            }
db.close();
            //
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if(guardado) {
                fab.setImageResource(android.R.drawable.star_big_on);
            }else{
                fab.setImageResource(android.R.drawable.star_big_off);
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (guardado) {
                            //delete
                            TablaUsuariosDatos cnx = new TablaUsuariosDatos(getApplicationContext(), "UsuarioDatos", null, 1);
                            SQLiteDatabase db = cnx.getWritableDatabase();
                            db.delete("Guardados", "codigo_usuario = " + DatosUsuarioLocal.Codigo_Usuario + " and codigo_libro = " + getIntent().getStringExtra("Codigo"), null);
                            fab.setImageResource(android.R.drawable.star_big_off);
                            Toast.makeText(getApplicationContext(), "Se ha eliminado de tus guardados.", Toast.LENGTH_LONG).show();
                            db.close();
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
                        datos.put("codigo_libro", getIntent().getStringExtra("Codigo"));
                        db.insert("Guardados", null, datos);
                        db.close();
                        Toast.makeText(getApplicationContext(), "Se ha guardado el libro", Toast.LENGTH_LONG).show();
                        fab.setImageResource(android.R.drawable.star_big_on);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Algo ha pasado.\nIntente más tarde", Toast.LENGTH_LONG).show();
                    }
                }
            });


       }catch (Exception e){
           e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG).show();
       }
    }
    public void EnviarRecibirDatos(String URL){
        try {
            //    Toast.makeText(getApplicationContext(), "" + URL, Toast.LENGTH_SHORT).show();
            Log.i("AQUI!!!",URL);
            JsonObjectRequest jsonObjectRequest;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if(error.toString().equalsIgnoreCase("com.android.volley.ParseError: org.json.JSONException: Value Registro of type java.lang.String cannot be converted to JSONObject")){
            Toast.makeText(getApplicationContext(),"Tu reseña ha sido registada correctamente",Toast.LENGTH_SHORT).show();
            dialog.hide();
            txtReseña.setEnabled(false);
            txtTituloReseña.setEnabled(false);
            CalificacionReseña.setEnabled(false);
            btnEnviarReseña.setEnabled(false);
            RecibirComentarios();
            return;
        }
        error.printStackTrace();
        fallos++;
        if(fallos==3){
            fallos=0;
        return;

        }

        /*if(uso==1){
            String url="https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor="+getIntent().getStringExtra("Genero")+"&donde=genero&tipo=general";
            url=url.replace(" ","%20");
            EnviarRecibirDatos(url);
        }*/
    }

    @Override
    public void onResponse(JSONObject response) {

    }
    public void RecibirComentarios(){
        String url = "https://libresoft.000webhostapp.com/volley/ListaOpiniones.php?codigo_libro="+getIntent().getStringExtra("Codigo");
        url = url.replace(" ", "%20");
        EnviarRecibirDatos(url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    rvComentarios.removeAllViewsInLayout();
                    final int size = listaReservas2.size();
                    listaComentarios.clear();
                    adapter.notifyItemRangeRemoved(0, size);
                    rlComen.setVisibility(View.VISIBLE);
                    tvcomentarios.setText("Comentarios:");

                    ElemenComentarios usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new ElemenComentarios();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        usuario.setReseña(jsonObject.optString("Reseña"));
                        usuario.setEstrellas(jsonObject.optString("Estrellas"));
                        usuario.setFecha(jsonObject.optString("Fecha"));
                        usuario.setNombreUsuario(jsonObject.optString("Usuario"));
                        usuario.setTituloReseña(jsonObject.optString("Titulo"));
                        usuario.setTipoUsuario(jsonObject.optString("Tipo"));
                        listaComentarios.add(usuario);
                        //AdaptadorComentarios adapter = new AdaptadorComentarios(listaComentarios);
                        rvComentarios.setAdapter(adapter);
                        fallos = 0;
                    }
                    if(listaComentarios.size()==0){
                        usuario.setNombreUsuario("No se han encontrado comentarios.");
                        listaComentarios.add(usuario);
                        AdaptadorComentarios adapter = new AdaptadorComentarios(listaComentarios);
                        rvComentarios.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                tvcomentarios.setText(tvcomentarios.getText()+"\nTodavía no se han encontraron comentarios.");
                rlComen.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
    }
    public void LibrosRelacionado(){
        String url="";
        if(getIntent().getStringExtra("Unidades").equalsIgnoreCase("")){
            url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + getIntent().getStringExtra     ("Genero") + "&donde=genero&tipo=general&order=calificacion";
        }else{
            url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd="+DatosUsuarioLocal.bd+"&tipo=1&valor=&donde=";
        }


   //     String url = "https://libresoft.000webhostapp.com/volley/ListaImagen.php?valor=" + getIntent().getStringExtra     ("Genero") + "&donde=genero&tipo=general&order=calificacion";
        url = url.replace(" ", "%20");
        EnviarRecibirDatos(url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Elemen usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new Elemen();
                        if(listaReservas2.size()>9){
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
                        if(getIntent().getStringExtra("Unidades").equalsIgnoreCase("")) {
                            usuario.setUnidades("");
                            usuario.setEstante("");
                            usuario.setBuscandoEn("");
                        }else {
                            usuario.setUnidades(jsonObject.optString("Unidades"));
                            usuario.setEstante(jsonObject.optString("Estante"));
                            usuario.setBuscandoEn(getIntent().getStringExtra("BuscandoEn"));
                        }
                        listaReservas2.add(usuario);
                        Adaptador adapter = new Adaptador(listaReservas2);
                        rvRel.setAdapter(adapter);
                        fallos = 0;

                    }
                    if(listaReservas2.size()==0){
                        usuario.setTitulo("No se han encontrado libros relacionados.");
                        listaReservas2.add(usuario);
                        Adaptador adapter = new Adaptador(listaReservas2);
                        rvRel.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
    public void BuscarLugares(){
        String url = "https://libresoft.000webhostapp.com/volley/BuscarLibrosEnLugares.php?titulo="+getIntent().getStringExtra("Titulo")+"&autor="+getIntent().getStringExtra("Autor")+"&editorial="+getIntent().getStringExtra("Editorial")+"&ciudad="+DatosUsuarioLocal.CiudadParaBuscar+"&estado="+DatosUsuarioLocal.EstadoParaBuscar;

         url=url.replace(" ","%20");

        EnviarRecibirDatos(url);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ElemenBusarLugares usuario = null;
                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {
                        usuario = new ElemenBusarLugares();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        if(jsonObject.optString("Version").equalsIgnoreCase("BibliotecaE")){
                           continue;
                        }
                        if(jsonObject.optString("Longuitud").equalsIgnoreCase("")||jsonObject.optString("Longuitud")==null||jsonObject.optString("Longuitud").equalsIgnoreCase(null)){
                            continue;
                        }

                        //       Toast.makeText(getApplicationContext(), jsonObject.optString("Direccion"), Toast.LENGTH_LONG).show();
                        usuario.setDireccion(jsonObject.optString("Direccion"));
                        usuario.setTitulo(jsonObject.optString("Titulo"));
                        usuario.setDisponibles(jsonObject.optString("Unidades"));
                        usuario.setNombreLugar(jsonObject.optString("Nombre"));
                        usuario.setPrecio(jsonObject.optString("Precio"));
                        usuario.setTipo(jsonObject.optString("Version"));
                        usuario.setOferta(jsonObject.optString("Oferta"));
                        usuario.setCiudad(jsonObject.optString("Ciudad"));
                        usuario.setEstado(jsonObject.optString("Estado"));
                        usuario.setEslogan(jsonObject.optString("Eslogan"));
                        usuario.setTelefono(jsonObject.optString("Telefono"));
                        usuario.setCorreo(jsonObject.optString("Correo"));
                        usuario.setLatitud(jsonObject.optString("Latitud"));
                        usuario.setLonguitud(jsonObject.optString("Longuitud"));
                        usuario.setBd(jsonObject.optString("bd"));
                        listaReservas1.add(usuario);
                        AdaptadorBuscarLugares adapter = new AdaptadorBuscarLugares(listaReservas1);
                        rv.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                disponibleen.setText(disponibleen.getText()+"\nNo ha sido encontrado en ningun lugar.");
                btnVerMapa.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        String Titulo= txtTituloReseña.getText().toString();
        String Reseña= txtReseña.getText().toString();
        float Cal = CalificacionReseña.getRating();
        if(Titulo.equals("")||Reseña.equals("")){
            Toast.makeText(getApplicationContext(),"No es posible dejar campos sin llenar.",Toast.LENGTH_LONG).show();
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Guardando reseña...");
        dialog.show();
        String codigo_libro = getIntent().getStringExtra("Codigo");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        String url = "https://libresoft.000webhostapp.com/volley/MeterOpinion.php?codigo_usuario="+ DatosUsuarioLocal.Codigo_Usuario+"&codigo_libro="+codigo_libro+"&titulo="+Titulo+"&opinion="+Reseña+"&estrellas="+Cal+"&fecha="+fecha;
        url = url.replace(" ","%20");
        Log.i("AQUIIIII",url);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.i("AQUI",response.toString());
                if(response.equalsIgnoreCase("Registro existosoRegistro existoso")){
                    Toast.makeText(getApplicationContext(),"La reseña ha sido registrada correctamente",Toast.LENGTH_SHORT).show();
                }else if (response.equalsIgnoreCase("Actualizacion existosa")){
                    Toast.makeText(getApplicationContext(),"La reseña se ha actualizado correctamente",Toast.LENGTH_SHORT).show();
                }
                dialog.hide();
                txtReseña.setText("");
                txtTituloReseña.setText("");
                CalificacionReseña.setRating(0);
                RecibirComentarios();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Toast.makeText(getApplicationContext(),"Error al conectar con el servidor.",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);


        //dialog.hide();

       // uso=3;
       // btnEnviarReseña.setEnabled(false);
        //rvComentarios.removeAllViewsInLayout();
       // EnviarRecibirDatos(url);
    }
}
