package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

import xyz.libresoft.libresoft_app.Adaptadores.Adaptador;
import xyz.libresoft.libresoft_app.Entidades.Elemen;

public class LibrosPrincipal extends AppCompatActivity  implements Response.Listener<JSONObject>,Response.ErrorListener {

    RecyclerView rv;
    ArrayList<Elemen> listaReservas1;
    int fallos=0;
    TextView txtTexto;
    String url="";

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_libros_principal);
            listaReservas1 = new ArrayList<>();
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
            }); toolbar.setTitle("Buscar");
            toolbar.setTitleTextColor(Color.WHITE);

            rv = (RecyclerView) findViewById(R.id.rv);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);

            //LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            //y esto en el xml
            //android:scrollbars="horizontal"
            rv.setLayoutManager(llm);
            txtTexto=findViewById(R.id.txtTexto);
            txtTexto.setText("Resultados para: '"+getIntent().getStringExtra("valor")+"'.\nBuscando por "+getIntent().getStringExtra("donde"));
            EnviarRecibirDatos(getIntent().getStringExtra("url"));

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public void EnviarRecibirDatos(String URL){
        try {
            dialog=new ProgressDialog(this);
            dialog.setMessage("Consultando Libros");
            dialog.setIcon(R.drawable.ic_action_reload);
            dialog.show();
       //    Toast.makeText(getApplicationContext(), "" + URL, Toast.LENGTH_SHORT).show();
            Log.i("AQUI!!!",URL);
            JsonObjectRequest jsonObjectRequest;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, this, this);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        if(error.toString().equalsIgnoreCase("com.android.volley.ParseError: org.json.JSONException: Value [] of type org.json.JSONArray cannot be converted to JSONObject")){
            Toast.makeText(getApplicationContext(),"No se encontró en la base de datos.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            return;
        }

        Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor.\nReintentando...", Toast.LENGTH_SHORT).show();
     //   Toast.makeText(getApplicationContext(),error.getMessage() , Toast.LENGTH_LONG).show();
        fallos++;
        if(fallos==3){
            Toast.makeText(getApplicationContext(), "El servidor no ha respondido después de "+fallos+" intentos.\nPruebe en otro momento.", Toast.LENGTH_LONG).show();
            fallos=0;
            dialog.hide();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            return;
        }
        dialog.hide();
        EnviarRecibirDatos(url);
    }


    @Override
    public void onResponse(JSONObject response) {
        try{
            Elemen usuario=null;
                JSONArray json=response.optJSONArray("usuario");
                try {
                    for (int i=0;i<json.length();i++){
                        usuario=new Elemen();
                        JSONObject jsonObject=null;
                        jsonObject=json.getJSONObject(i);
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
                        usuario.setCodigo(jsonObject.optString("Codigo"));
                        usuario.setEstrellas(jsonObject.optString("Estrellas"));
                        usuario.setUnidades("");
                        usuario.setEstante("");
                        usuario.setBuscandoEn("");
                        //    Toast.makeText(getApplicationContext(),jsonObject.optString("Imagen") , Toast.LENGTH_LONG).show();
                        listaReservas1.add(usuario);
                        dialog.hide();
                    }
                    //        pg.hide();
                    Adaptador adapter=new Adaptador(listaReservas1);
                    rv.setAdapter(adapter);
                    txtTexto.setText(txtTexto.getText().toString()+" --> Encontrados: "+listaReservas1.size());
                    fallos=0;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                        " "+response, Toast.LENGTH_LONG).show();

            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



}
