package xyz.libresoft.libresoft_app.Mapas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import xyz.libresoft.libresoft_app.R;

public class MapasLugaresGeneralGeneral extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    Marker mk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas_lugares_general_general);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String [] Nombre = getIntent().getStringArrayExtra("Nombre");
        String [] Direccion = getIntent().getStringArrayExtra("Direccion");
        String [] Latitud = getIntent().getStringArrayExtra("Latitud");
        String [] Longuitud = getIntent().getStringArrayExtra("Longuitud");
        String [] Version = getIntent().getStringArrayExtra("Version");
        int direimg=0;
        LatLng Lugar=null;
        for(int i = 0;i<Version.length;i++) {
            Lugar = new LatLng(Float.parseFloat(Latitud[i]),Float.parseFloat(Longuitud[i]));
            if(Version[i].equalsIgnoreCase("Biblioteca")){
                direimg=R.drawable.library;
            }else{
                direimg=R.drawable.marcalibroabierto;
            }
            mk = googleMap.addMarker(new MarkerOptions()
                    .position(Lugar)
                    .title(Nombre[i])
                    .snippet(Direccion[i])
                    .icon(BitmapDescriptorFactory.fromResource(direimg))
            );
            mk.showInfoWindow();
        }

        googleMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lugar,15));

        googleMap.setOnMarkerClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        mMap.setMyLocationEnabled(true);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            int posicion = 0;
            String[] Nombre = getIntent().getStringArrayExtra("Nombre");
            String[] Tipo = getIntent().getStringArrayExtra("Tipo");
            String[] Eslogan = getIntent().getStringArrayExtra("Eslogan");
            String[] Correo = getIntent().getStringArrayExtra("Correo");
            String[] Tel = getIntent().getStringArrayExtra("Tel");
            for (int i = 0; i < Nombre.length; i++) {
                if (marker.getTitle().equals(Nombre[i])) {
                    posicion = i;
                }
            }
            final CharSequence[] opciones = {"Cerrar"};
            final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
            alertOpciones.setTitle(marker.getTitle());
            String ofe = "";

            int ico = R.drawable.librerias;
            String title = marker.getSnippet() + "\n'" + Eslogan[posicion] + "'\nTel: " + Tel[posicion] + "\nCorreo: " + Correo[posicion];
            if (Tipo[posicion].equalsIgnoreCase("Biblioteca")) {
                title = marker.getSnippet() + "\nConsulta libre" + "\n'" + Eslogan[posicion] + "'\nTel: " + Tel[posicion] + "\nCorreo: " + Correo[posicion];
                ico = R.drawable.bibliotecas;
            }
            Log.i("AQUI",title);
            alertOpciones.setMessage(title);
            alertOpciones.setIcon(ico);
            alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i].equals("Cerrar")) {
                        dialogInterface.dismiss();
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            });
            alertOpciones.show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {



        return false;
    }
}

