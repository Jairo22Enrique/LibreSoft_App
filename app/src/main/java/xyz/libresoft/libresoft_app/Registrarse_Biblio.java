package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;

public class Registrarse_Biblio extends AppCompatActivity {
    Spinner turno;
    Button btnregis;
    EditText txtNombre,txtGrupo, txtEdad,txtGrado,txtNo,txtTel;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse__biblio);
        try {
            txtEdad = findViewById(R.id.txtEdad);
            txtGrupo = findViewById(R.id.txtGrupo);
            txtTel = findViewById(R.id.txtTel);
            txtGrado = findViewById(R.id.txtGrado);
            txtNombre = findViewById(R.id.txtNombre);
            txtNo = findViewById(R.id.txtNo);


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
            toolbar.setTitle("Registro");
            turno = findViewById(R.id.turno);
            String[] turnos = {"Matutino", "Vespertino"};
            ArrayAdapter<String> adaptacion = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_propio, turnos);
            turno.setAdapter(adaptacion);
            btnregis = findViewById(R.id.btnRegistrar);
            btnregis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog = new ProgressDialog(getApplicationContext());
                        dialog.setMessage("Procesando...");

//                        dialog.show();
                        String url = "https://libresoft.000webhostapp.com/volley/MeterUsuarioBiblioteca.php?nombre=" + txtNombre.getText().toString() + "&bd=" + DatosUsuarioLocal.bd + "&grado=" + txtGrado.getText().toString() + "&grupo=" + txtGrupo.getText().toString() + "&turno=" + turno.getSelectedItem().toString() + "&nocontrol=" + txtNo.getText().toString() + "&edad=" + txtEdad.getText().toString() + "+&tel=" + txtTel.getText().toString()+"&codigo="+DatosUsuarioLocal.Codigo_Usuario;
                        url = url.replace(" ", "%20");
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("Registro existoso")) {
                                    Toast.makeText(getApplicationContext(), "Registro existoso", Toast.LENGTH_SHORT).show();
                                }
                                dialog.hide();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.hide();
                                Toast.makeText(getApplicationContext(), "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                    }


            });
           /* if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Dueño de libreria")){
                btnregis.setVisibility(View.INVISIBLE);
            }*/
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
