package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorBuscarLugares;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;

public class Registrar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    Button registrar;
    Spinner tipoUsuario,Estado,Ciudad;
    EditText txtCorreo,txtContra,txtConfirmar,txtCUIL,txtNombre;
    ProgressDialog dialog;
    private OnFragmentInteractionListener mListener;

    public Registrar() {
        // Required empty public constructor
    }

    public static Registrar newInstance(String param1, String param2) {
        Registrar fragment = new Registrar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_registrar, container, false);

        tipoUsuario = vista.findViewById(R.id.tipoUsuario);
        txtCorreo = vista.findViewById(R.id.txtCorreo);
        txtConfirmar = vista.findViewById(R.id.txtConfirmar);
        txtContra = vista.findViewById(R.id.txtContra);
        txtCUIL = vista.findViewById(R.id.txtCUIL);
        Estado = vista.findViewById(R.id.Estado);
        Ciudad = vista.findViewById(R.id.Ciudad);
        txtNombre = vista.findViewById(R.id.txtNombre);
        txtCUIL.setVisibility(View.INVISIBLE);
        registrar= vista.findViewById(R.id.registrar);

        String[] opciones1={"Usuario","Estudiante","Maestro","Bibliotecario","Dueño de libreria"};
        ArrayAdapter<String> adaptacion1 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,opciones1);
        tipoUsuario.setAdapter(adaptacion1);
        tipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!tipoUsuario.getSelectedItem().toString().equalsIgnoreCase("Usuario")){
                   txtCUIL.setVisibility(View.VISIBLE);
                }else{
                    txtCUIL.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(Estado.getSelectedItem().toString().equalsIgnoreCase("Michoacan")){
                    String[]  Cuidades={"Zitacuaro"};
                    ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,Cuidades);
                    Ciudad.setAdapter(adaptacion3);
                }
                else if(Estado.getSelectedItem().toString().equalsIgnoreCase("Yucatan")){
                    String[]  Cuidades={"Merida"};
                    ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,Cuidades);
                    Ciudad.setAdapter(adaptacion3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] Estados={"Michoacan","Yucatan"};
        ArrayAdapter<String> adaptacion2 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,Estados);
        Estado.setAdapter(adaptacion2);

        String[] Cuidades={"Zitacuaro","Merida"};
        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,Cuidades);
        Ciudad.setAdapter(adaptacion3);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtContra.getText().toString().equals("")||txtNombre.getText().toString().equals("")||txtCorreo.getText().toString().equals("")||txtConfirmar.getText().toString().equals("")){
                    Toast.makeText(getContext(),"No puede haber campos vacíos.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!txtContra.getText().toString().equals(txtConfirmar.getText().toString())){
                    Toast.makeText(getContext(),"Las contraseñas no coinciden.",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new ProgressDialog(getContext());
                dialog.setMessage("Procesando...");
                dialog.show();



                if(tipoUsuario.getSelectedItem().toString().equalsIgnoreCase("Usuario")){
                    String   url ="http://libresoft.000webhostapp.com/volley/MeterUsuario.php?correo="+txtCorreo.getText().toString()+"&nombre="+txtNombre.getText().toString()+"&cuil="+txtCUIL.getText().toString()+"&estado="+Estado.getSelectedItem().toString()+"&ciudad="+Ciudad.getSelectedItem().toString()+"&contra="+txtContra.getText().toString()+"&tipo="+tipoUsuario.getSelectedItem().toString()+"&lugar=n/a";
                    url=url.replace(" ","%20");
                    url = url.replace(" ", "%20");
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Registro existoso")){
                                Toast.makeText(getContext(),"Registro existoso",Toast.LENGTH_SHORT).show();
                            }else if (response.equalsIgnoreCase("Correo repetido")){
                                Toast.makeText(getContext(),"El correo ya ha sido registrado",Toast.LENGTH_SHORT).show();
                            }
                            dialog.hide();
                        }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.hide();
                            Toast.makeText(getContext(),"Error al conectar con el servidor.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);
return;
                }


                //-----
                if(txtCUIL.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Proporciona un CUIL.",Toast.LENGTH_SHORT).show();
                    dialog.hide();
                    return;
                }
                String   url1 ="https://libresoft.000webhostapp.com/volley/ObtenerLugarCUIL.php?cuil="+txtCUIL.getText().toString();
                url1=url1.replace(" ","%20");
                url1 = url1.replace(" ", "%20");
                RequestQueue queue1 = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url1 ,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("CUIL inconrrecto")){
                            Toast.makeText(getContext(),"CUIL incorrecto.",Toast.LENGTH_SHORT).show();
                            dialog.hide();
                            return;
                        }
                        String   url ="http://libresoft.000webhostapp.com/volley/MeterUsuario.php?correo="+txtCorreo.getText().toString()+"&nombre="+txtNombre.getText().toString()+"&cuil="+txtCUIL.getText().toString()+"&estado="+Estado.getSelectedItem().toString()+"&ciudad="+Ciudad.getSelectedItem().toString()+"&contra="+txtContra.getText().toString()+"&tipo="+tipoUsuario.getSelectedItem().toString()+"&lugar="+response;
                        url=url.replace(" ","%20");
                        url = url.replace(" ", "%20");
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("Registro existoso")){
                                    Toast.makeText(getContext(),"Registro existoso",Toast.LENGTH_SHORT).show();
                                }else if (response.equalsIgnoreCase("Correo repetido")){
                                    Toast.makeText(getContext(),"El correo ya ha sido registrado",Toast.LENGTH_SHORT).show();
                                }
                                dialog.hide();
                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.hide();
                                Toast.makeText(getContext(),"Error al conectar con el servidor.",Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);


                        dialog.hide();
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.hide();
                        Toast.makeText(getContext(),"Error al conectar con el servidor.",Toast.LENGTH_SHORT).show();
                    }
                });
                queue1.add(stringRequest);

            }
        });

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
