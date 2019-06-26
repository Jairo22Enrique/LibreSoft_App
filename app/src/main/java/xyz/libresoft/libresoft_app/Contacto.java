package xyz.libresoft.libresoft_app;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Contacto extends AppCompatActivity {
TextView telJairo,telGos,correoLibre,correoGos,correoJairo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        try {
            //setTitle("Contacto");
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
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("Contacto");
            TextView text = findViewById(R.id.texto);
            text.setText("Para cualquier aclaración, duda o error contactenos a:");
            ImageView mail = findViewById(R.id.mail);
            ImageView cel = findViewById(R.id.cel);
            telJairo=findViewById(R.id.textView10);
            telGos=findViewById(R.id.textView11);
            correoLibre=findViewById(R.id.textView8);
            correoJairo=findViewById(R.id.textView13);
            correoGos=findViewById(R.id.textView14);
            correoJairo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] TO = {correoJairo.getText().toString()}; //aquí pon tu correo
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
// Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Enviar email a "+correoJairo.getText().toString()));
                        // finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),
                                "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            correoGos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] TO = {correoGos.getText().toString()}; //aquí pon tu correo
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
// Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Enviar email a "+correoGos.getText().toString()));
                        // finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),
                                "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            correoLibre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] TO = {correoLibre.getText().toString()}; //aquí pon tu correo
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
// Esto podrás modificarlo si quieres, el asunto y el cuerpo del mensaje
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Enviar email a "+correoLibre.getText().toString()));
                       // finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),
                                "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            telJairo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dial = "tel:" + telJairo.getText().toString();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
            });
            telGos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dial = "tel:" + telGos.getText().toString();
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
