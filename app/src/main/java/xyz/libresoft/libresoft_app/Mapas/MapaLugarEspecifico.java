package xyz.libresoft.libresoft_app.Mapas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import xyz.libresoft.libresoft_app.R;

public class MapaLugarEspecifico extends FragmentActivity implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener,  GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    Marker mk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_lugar_especifico);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

float Latitud = Float.parseFloat(getIntent().getStringExtra("Latitud"));
        float Longuitud = Float.parseFloat(getIntent().getStringExtra("Longuitud"));
        String tipo = getIntent().getStringExtra("Tipo");
        int direimg=0;
        if(tipo.equalsIgnoreCase("Biblioteca")){
            direimg=R.drawable.library;
        }else{
            direimg=R.drawable.marcalibroabierto;
        }
        LatLng Lugar = new LatLng(Latitud, Longuitud);
        mk = googleMap.addMarker(new MarkerOptions()
                .position(Lugar)
                .title(getIntent().getStringExtra("NombreLugar"))
                .snippet(getIntent().getStringExtra("Direccion"))
                .icon(BitmapDescriptorFactory.fromResource(direimg))
        );
        mk.showInfoWindow();
googleMap.setOnInfoWindowClickListener(this);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lugar,15));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String  Titulo = getIntent().getStringExtra("Titulo");
        String  Tipo = getIntent().getStringExtra("Tipo");
      //  String  Disponibles = getIntent().getStringExtra("Disponibles");
       // String  Precio = getIntent().getStringExtra("Precio");
       // String  Oferta = getIntent().getStringExtra("Oferta");
        final CharSequence[] opciones={"Cerrar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
        alertOpciones.setTitle(marker.getTitle());
        String ofe="";

        int ico = R.drawable.librerias;
        String title=marker.getSnippet()+"\n'"+getIntent().getStringExtra("Eslogan")+"'\nTel: "+getIntent().getStringExtra("Telefono")+"\nCorreo: "+getIntent().getStringExtra("Correo");
        if(Tipo.equalsIgnoreCase("Biblioteca")){
            title=marker.getSnippet()+"\nConsulta libre"+"\n'"+getIntent().getStringExtra("Eslogan")+"'\nTel: "+getIntent().getStringExtra("Telefono")+"\nCorreo: "+getIntent().getStringExtra("Correo");
            ico = R.drawable.bibliotecas;
        }
        alertOpciones.setMessage(title);
        alertOpciones.setIcon(ico);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Cerrar")){
                    dialogInterface.dismiss();
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }
}
