package xyz.libresoft.libresoft_app.Mapas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.MainActivity;
import xyz.libresoft.libresoft_app.R;

public class MapasMeterUbicacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    String latitude = "";
    String longuitude = "";
    Marker mk;
    ImageButton btnListo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas_meter_ubicacion);

        ImageButton btnListo= findViewById(R.id.btnListo);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle(getIntent().getStringExtra("Nombre"));
        Log.i("AQUI",getIntent().getStringExtra("Nombre"));
        btnListo=findViewById(R.id.btnListo);
        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://libresoft.000webhostapp.com/volley/MeterActualizarCoordenadas.php?bd="+ DatosUsuarioLocal.bd+"&lat="+latitude+"&lon="+longuitude;
                url=url.replace(" ","%20");
                RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url ,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                                if(response.equalsIgnoreCase("Registro existoso")){
                                    Toast.makeText(getApplicationContext(),"Se ha modificado correctamente",Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error al conectar con el servidor.",Toast.LENGTH_SHORT).show();
                    }
                });
                queue1.add(stringRequest);

            }
        });
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
        googleMap.setOnMarkerDragListener(this);
        // Add a marker in Sydney and move the camera
        double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        double lon = Double.parseDouble(getIntent().getStringExtra("long"));
        latitude = lat + "";
        longuitude = lon + "";
        LatLng sydney = new LatLng(lat, lon);
        mk = googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Ubicacion Actual.")
                .snippet("Arrastra hacia el lugar que quieras mostrar.")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcalibroabierto))
                .draggable(true)
        );
        mk.showInfoWindow();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        Toast.makeText(getApplicationContext(),"Arrastrar el marcador hasta la ubicación deseada y darle en 'Listo'.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude=marker.getPosition().latitude+"";
        longuitude=marker.getPosition().longitude+"";
    }
    //Cock

}