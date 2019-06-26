package xyz.libresoft.libresoft_app.Lector;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import xyz.libresoft.libresoft_app.Adaptadores.Adaptador;
import xyz.libresoft.libresoft_app.DatosLibros;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.R;

public class CamaraLector extends AppCompatActivity implements View.OnClickListener{

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    ProgressDialog dialog;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_camara_lector);

            cameraView = (SurfaceView) findViewById(R.id.surfaceView);
            textView = (TextView) findViewById(R.id.tv);
            Button btn = findViewById(R.id.OK);
            btn.setOnClickListener(this);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!textRecognizer.isOperational()) {
                Log.w("MainActivity", "Detector dependencies are not yet available");
            } else {
                try{
                cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(2.0f)
                        .setAutoFocusEnabled(true)
                        .build();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {

                        try {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(CamaraLector.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        RequestCameraPermissionID);
                                return;
                            }
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        cameraSource.stop();
                    }
                });

                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {

                        final SparseArray<TextBlock> items = detections.getDetectedItems();
                        if (items.size() != 0) {
                            textView.post(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 0; i < items.size(); ++i) {
                                        TextBlock item = items.valueAt(i);
                                        stringBuilder.append(item.getValue());
                                        //  stringBuilder.append("\n");
                                    }

                                    textView.setText(stringBuilder.toString().substring(stringBuilder.toString().length()-4));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                      //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }

                });

            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
    void mandar(String text){
        try {
            dialog=new ProgressDialog(this);
            dialog.setMessage("Buscando...");
            dialog.show();
            String url = "https://libresoft.000webhostapp.com/volley/ObtenerLibrosLugar.php?bd="+ DatosUsuarioLocal.bd+"&valor="+text+"&donde=codigo&tipo=Libreria";
            Log.i("AQUI",url);
            url = url.replace(" ", "%20");
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null ,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray json = response.optJSONArray("usuario");
                        for (int a = 0; a < json.length(); a++) {
                            JSONObject jsonObject = null;
                            jsonObject = json.getJSONObject(a);
                            Intent i = new Intent(getApplicationContext(), DatosLibros.class);
                            i.putExtra("Titulo",jsonObject.optString("Titulo"));
                            i.putExtra("Autor",jsonObject.optString("Autor"));
                            i.putExtra("Genero",jsonObject.optString("Genero"));
                            i.putExtra("Imagen",jsonObject.optString("Imagen"));
                            i.putExtra("Editorial",jsonObject.optString("Editorial"));
                            i.putExtra("Precio",jsonObject.optString("Precio"));
                            i.putExtra("Lugar",jsonObject.optString("Lugar"));
                            i.putExtra("Descripcion",jsonObject.optString("Descripcion"));
                            i.putExtra("Año",jsonObject.optString("Ano"));
                            i.putExtra("Edicion",jsonObject.optString("Edicion"));
                            i.putExtra("Estrellas",jsonObject.optString("Estrellas"));
                            i.putExtra("Codigo",jsonObject.optString("Codigo"));
                            i.putExtra("Estante", jsonObject.optString("Estante"));
                            i.putExtra("Unidades",jsonObject.optString("Unidades"));
                            i.putExtra("Oferta", "");
                            i.putExtra("BuscandoEn",DatosUsuarioLocal.bd);
                            startActivity(i);
                            dialog.hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.hide();
                        Toast.makeText(getApplicationContext(),e.getMessage()+"\n205",Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.hide();
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            queue.add(stringRequest);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException e) {
            resultado = false;
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return resultado;
    }

    @Override
    public void onClick(View view) {
        try {
            String text = textView.getText().toString();
            text = text.replace(" ", "");
            //  text = text.replace("\n","");
            text = text.substring(text.length() - 4, text.length());
//            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            if (isNumeric(text)) {
                mandar(text);
            } else {
                //  Toast.makeText(this, "No es un número", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Código no válido", Toast.LENGTH_SHORT).show();

        }
    }
}
