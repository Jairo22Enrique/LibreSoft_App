package xyz.libresoft.libresoft_app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import xyz.libresoft.libresoft_app.Adaptadores.AdaptadorBuscarLugares;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.Mapas.BuscarUbicacion;
import xyz.libresoft.libresoft_app.Mapas.MapasMeterUbicacion;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatosDueno.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatosDueno#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosDueno extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    View vista;
    Spinner Estado,Ciudad;
    Button btnAñadirU;
    EditText txtNombreLugar,txtDireccion,txtEslogan,txtTel;
    TextView txtCorreo;
    String latiud="",longuitud="",estado="",ciudad="";
    private String mParam1;
    private String mParam2;
    ProgressDialog dialog;

    private OnFragmentInteractionListener mListener;

    public DatosDueno() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosDueno.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosDueno newInstance(String param1, String param2) {
        DatosDueno fragment = new DatosDueno();
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
        vista=inflater.inflate(R.layout.fragment_datos_dueno, container, false);

        try {
            Estado = vista.findViewById(R.id.estado2);
            Ciudad = vista.findViewById(R.id.ciudad2);
            txtDireccion = vista.findViewById(R.id.txtDireccion);
            txtEslogan = vista.findViewById(R.id.txtEslogan);
            txtNombreLugar = vista.findViewById(R.id.txtNombreLugar);
            txtTel = vista.findViewById(R.id.txtTel);
            txtCorreo = vista.findViewById(R.id.txtCorreo);

            btnAñadirU=vista.findViewById(R.id.btnAnadirU);
            btnAñadirU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(longuitud.equalsIgnoreCase("")||longuitud.equalsIgnoreCase("null")||
                            longuitud==null){
                        Intent i = new Intent(getContext(), BuscarUbicacion.class);
                        i.putExtra("Nombre",txtNombreLugar.getText().toString());
                        startActivity(i);

                    }else{
                        Intent i = new Intent(getContext(), MapasMeterUbicacion.class);
                        i.putExtra("lat",latiud);
                        i.putExtra("long",longuitud);
                        i.putExtra("Nombre",txtNombreLugar.getText().toString());

                        startActivity(i);
                    }

                }
            });
            if(DatosUsuarioLocal.Tipo.equalsIgnoreCase("Estudiante")||DatosUsuarioLocal.Tipo.equalsIgnoreCase("Maestro")){
                btnAñadirU.setVisibility(View.GONE);
            }



dialog=new ProgressDialog(getContext());
            dialog.setMessage("Obteniendo datos..");
            TraerDatos();


        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return vista;
    }

    private void TraerDatos() {
    String url = "https://libresoft.000webhostapp.com/volley/ObtenerLugarNombre.php?nombre="+ DatosUsuarioLocal.bd+"&e=e";
    url=url.replace(" ","%20");
    Log.i("AQUI",url);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray json = response.optJSONArray("usuario");
                    for (int i = 0; i < json.length(); i++) {

                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        //       Toast.makeText(getApplicationContext(), jsonObject.optString("Direccion"), Toast.LENGTH_LONG).show();
                        txtDireccion.setText(jsonObject.optString("Direccion"));
                        ciudad=(jsonObject.optString("Ciudad"));
                        estado=jsonObject.optString("Estado");
                        txtEslogan.setText(jsonObject.optString("Eslogan"));
                        txtTel.setText(jsonObject.optString("Telefono"));
                        txtCorreo.setText(jsonObject.optString("Correo"));
                        latiud=(jsonObject.optString("Latitud"));
                        longuitud=(jsonObject.optString("Longuitud"));
                        txtNombreLugar.setText(jsonObject.optString("Nombre"));
                        if(longuitud.equalsIgnoreCase("")||longuitud.equalsIgnoreCase("null")||
                                longuitud==null){
                            btnAñadirU.setText("Añadir Ubicacion");
                        }

                        String[] Estados={estado};
                        ArrayAdapter<String> adaptacion2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,Estados);
                        Estado.setAdapter(adaptacion2);

                        String[] Cuidades={ciudad};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio,Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                        dialog.hide();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
dialog.hide();
            }
        });
        queue.add(stringRequest);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //--Localización

}
