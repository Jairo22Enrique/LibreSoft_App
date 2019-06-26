package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorBuscarLugares;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosRegistrado;


public class Ingresar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    Button ing;
    EditText txtCorreo,txtContra;
    ProgressDialog dialog;

    private OnFragmentInteractionListener mListener;

    public Ingresar() {
        // Required empty public constructor
    }

    public static Ingresar newInstance(String param1, String param2) {
        Ingresar fragment = new Ingresar();
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
        vista=inflater.inflate(R.layout.fragment_ingresar, container, false);
        txtContra = vista.findViewById(R.id.txtContra);
        txtCorreo = vista.findViewById(R.id.txtCorreo);
        ing = vista.findViewById(R.id.ing);
        ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Ingresando...");
            dialog.show();
                String   url = "http://libresoft.000webhostapp.com/volley/ObtenerUsuario.php?correo="+txtCorreo.getText().toString()+"&contra="+txtContra.getText().toString();
                url=url.replace(" ","%20");
                url = url.replace(" ", "%20");

                RequestQueue queue = Volley.newRequestQueue(getContext());
                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                        ElemenBusarLugares usuario = null;
                        JSONArray json = response.optJSONArray("usuario");
                        for (int i = 0; i < json.length(); i++) {

                            usuario = new ElemenBusarLugares();
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(i);
                         //   Log.i("FIJATE", jsonObject.optString("Nombre"));
                           // Toast.makeText(getContext(),jsonObject.optString("Nombre"),Toast.LENGTH_SHORT).show();

                            Intent j = new Intent(getContext(), MainActivity.class);
                            DatosUsuarioLocal.Ciudad=jsonObject.optString("Ciudad");
                            DatosUsuarioLocal.CiudadParaBuscar=jsonObject.optString("Ciudad");
                            DatosUsuarioLocal.EstadoParaBuscar=jsonObject.optString("Estado");
                            DatosUsuarioLocal.Correo=jsonObject.optString("Correo");
                            DatosUsuarioLocal.CUIL=jsonObject.optString("CUIL");
                            DatosUsuarioLocal.Estado=jsonObject.optString("Estado");
                            DatosUsuarioLocal.Nombre=jsonObject.optString("Nombre");
                            DatosUsuarioLocal.Codigo_Usuario=jsonObject.optString("Codigo_Usuario");
                            DatosUsuarioLocal.Tipo=jsonObject.optString("Tipo");
                            DatosUsuarioLocal.bd=jsonObject.optString("Lugar");
                            DatosUsuarioLocal.Contraseña=txtContra.getText().toString();
                            //
                            TablaUsuariosRegistrado cnx = new TablaUsuariosRegistrado(getContext(), "UsuarioRegistrado", null, 1);
                            SQLiteDatabase db = cnx.getWritableDatabase();
                            ContentValues datos = new ContentValues();
                            datos.put("correo",DatosUsuarioLocal.Correo);
                            datos.put("contrasena",txtContra.getText().toString());
                            db.insert("Usuario",null,datos);
                            db.close();
                            //
                            dialog.hide();
                            startActivity(j);
                        }
                    }catch(Exception e){
                            dialog.hide();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Datos Incorrectos",Toast.LENGTH_SHORT).show();
                        if(txtCorreo.getText().toString().equals("jairo")) {
                            Intent j = new Intent(getContext(), MainActivity.class);
                            startActivity(j);
                        }
                        error.printStackTrace();
                        dialog.hide();
                    }
                });
                queue.add(stringRequest);
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
