package xyz.libresoft.libresoft_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosRegistrado;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatosUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatosUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosUsuario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    View vista;
    Spinner Estado,Ciudad;
    EditText txtNombre,txtCuil,txtActual,txtNueva;
    TextView txtCorreo;
    Button btnGuardar,btnCambiar;
    ProgressDialog dialog;
    String lugar="";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DatosUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosUsuario newInstance(String param1, String param2) {
        DatosUsuario fragment = new DatosUsuario();
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
        vista=inflater.inflate(R.layout.fragment_datos_usuario, container, false);

        try {
            btnGuardar=vista.findViewById(R.id.btnGuardar);
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog=new ProgressDialog(getContext());
                    dialog.setMessage("Guardando cambios...");
                    dialog.show();
                    if(txtCuil.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Proporciona un CUIL.",Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        return;
                    }
                    String   url1 ="https://libresoft.000webhostapp.com/volley/ObtenerLugarCUIL.php?cuil="+txtCuil.getText().toString();
                    url1=url1.replace(" ","%20");

                    RequestQueue queue1 = Volley.newRequestQueue(getContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url1 ,new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("CUIL inconrrecto")){
                                Toast.makeText(getContext(),"CUIL incorrecto.",Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                return;
                            }
                            String contra="";
                            if(txtActual.getVisibility()==View.GONE){
                                contra=DatosUsuarioLocal.Contraseña;
                            }else{
                                if(txtActual.getText().toString().equalsIgnoreCase("")||txtNueva.getText().toString().equalsIgnoreCase("")){
                                    Toast.makeText(getContext(),"No dejes campos en blanco.",Toast.LENGTH_SHORT).show();
                                    dialog.hide();
                                    return;
                                }
                                if(!txtActual.getText().toString().equals(DatosUsuarioLocal.Contraseña)){
                                    Toast.makeText(getContext(),"Las contraseña no coincide con la  actual.",Toast.LENGTH_SHORT).show();
                                    dialog.hide();
                                    return;
                                }
                                contra=txtNueva.getText().toString();
                            }

                            String url = "http://libresoft.000webhostapp.com/volley/ActualizarDatosUsuarioApp.php?nombre="+txtNombre.getText()                  .toString()+"&correo="+txtCorreo.getText().toString()+"&contra="+contra+"&cuil="+txtCuil.getText().toString()                               +"&ciudad="+Ciudad.getSelectedItem().toString()+"&estado="+Estado.getSelectedItem().toString()+"&lugar="+response;
                            lugar=response;
                url=url.replace(" ","%20");
                            Log.i("AQUI",url);
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equalsIgnoreCase("Registro existoso")) {
                                        Toast.makeText(getContext(), "Los cambios se han guardado correctamente", Toast.LENGTH_SHORT).show();
                                        DatosUsuarioLocal.Ciudad=Ciudad.getSelectedItem().toString();
                                        DatosUsuarioLocal.CiudadParaBuscar=Ciudad.getSelectedItem().toString();
                                        DatosUsuarioLocal.EstadoParaBuscar=Estado.getSelectedItem().toString();
                                        DatosUsuarioLocal.Correo=txtCorreo.getText().toString();
                                        DatosUsuarioLocal.CUIL=txtCuil.getText().toString();
                                        DatosUsuarioLocal.Estado=Estado.getSelectedItem().toString();
                                        DatosUsuarioLocal.Nombre=txtNombre.getText().toString();
                                        DatosUsuarioLocal.bd=lugar;
                                        if(txtActual.getVisibility()!=View.GONE) {
                                            try {
                                                TablaUsuariosRegistrado cnx = new TablaUsuariosRegistrado(getContext(), "UsuarioRegistrado", null, 1);
                                                SQLiteDatabase db = cnx.getWritableDatabase();
                                                ContentValues up = new ContentValues();
                                                up.put("contrasena", txtNueva.getText().toString());
                                                db.update("Usuario", up, "correo = '" + txtCorreo.getText().toString()+"'", null);
                                                db.close();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                              //  Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                        final CharSequence[] opciones={"Ok"};
                                        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                        builder.setTitle("Los relacionados con el CUIL se aplicarán la próxima vez que inicie\nla app");
                                        builder.setIcon(R.drawable.ic_action_reload);
                                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        builder.show();
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
            btnCambiar=vista.findViewById(R.id.btnCambiar);
            btnCambiar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtActual.getVisibility()==View.GONE){
                        txtActual.setVisibility(View.VISIBLE);
                        txtNueva.setVisibility(View.VISIBLE);
                    }else{
                        txtNueva.setVisibility(View.GONE);
                        txtActual.setVisibility(View.GONE);
                    }
                }
            });
            txtCorreo = vista.findViewById(R.id.txtCorreo);
            txtCuil = vista.findViewById(R.id.txtCuil);
            txtNombre = vista.findViewById(R.id.txtNombre);
            txtActual = vista.findViewById(R.id.txtActual);
            txtNueva = vista.findViewById(R.id.txtNueva);
            txtNueva.setVisibility(View.GONE);
            txtActual.setVisibility(View.GONE);
            Cargar();


            Estado = vista.findViewById(R.id.estado);
            Ciudad = vista.findViewById(R.id.ciudad);

            Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (Estado.getSelectedItem().toString().equalsIgnoreCase("Michoacan")) {
                        String[] Cuidades = {"Zitacuaro"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio, Cuidades);
                        Ciudad.setAdapter(adaptacion3);
                    } else if (Estado.getSelectedItem().toString().equalsIgnoreCase("Yucatan")) {
                        String[] Cuidades = {"Merida"};
                        ArrayAdapter<String> adaptacion3 = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_propio, Cuidades);
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
            InstSpinner();

        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return vista;
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
    void Cargar(){
        txtCorreo.setText(DatosUsuarioLocal.Correo);
        txtCuil.setText(DatosUsuarioLocal.CUIL);
        txtNombre.setText(DatosUsuarioLocal.Nombre);

    }
}
/*
<?php
header( 'Content-Type: text/html;charset=utf-8' );
$hostname_localhost ="libresoft.cyfqilcoefvh.us-east-2.rds.amazonaws.com";
$database_localhost ="Registro_Usuarios";
$username_localhost ="root";
$password_localhost ="123456789";

$nombre = $_GET['nombre'];
$correo = $_GET['correo'];
$contra = $_GET['contra'];
$cuil = $_GET['cuil'];
$ciudad = $_GET['ciudad'];
$estado = $_GET['estado'];
$json=array();

		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
mysqli_set_charset($conexion,"utf8");


$consulta="UPDATE Usuarios_App SET nombre = '$nombre', Contrasena = '$contra', CUIL = '$cuil', ciudad = '$ciudad', estado = '$estado' WHERE correo = '$correo'";
$resultad=mysqli_query($conexion,$consulta);

		  if(mysqli_affected_rows($conexion)>0){
echo "Registro existoso";
}else{
die(mysqli_error($conexion));
}
		mysqli_close($conexion);

?>

 */