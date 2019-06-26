package xyz.libresoft.libresoft_app.Adaptadores;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.libresoft.libresoft_app.Clases.Conexion;
import xyz.libresoft.libresoft_app.DatosLibros;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.Entidades.ElemenRegistro_Ventas;
import xyz.libresoft.libresoft_app.R;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

/**
 * Created by azulm on 21/04/2018.
 */

public class AdaptadorRegistro_Ventas extends RecyclerView.Adapter<AdaptadorRegistro_Ventas.adaptadorHolder>{

    List<ElemenRegistro_Ventas> ListaElemen;

    public AdaptadorRegistro_Ventas(List<ElemenRegistro_Ventas> listaElemen) {
        this.ListaElemen = listaElemen;
    }

    @Override
    public adaptadorHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementoregistro_ventas,parent,false);
        final adaptadorHolder holder = new adaptadorHolder(v);

holder.contenedor.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        final CharSequence[] opciones={"Cerrar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(parent.getContext());
        builder.setTitle(ListaElemen.get(holder.getAdapterPosition()).getTitulo());
        builder.setIcon(R.drawable.ic_action_ventas);
        builder.setMessage(
                ListaElemen.get(holder.getAdapterPosition()).getFecha_venta()+
                "\nVendidos: "+ListaElemen.get(holder.getAdapterPosition()).getVendidos()+
                "\nTotal Venta: $"+ListaElemen.get(holder.getAdapterPosition()).getTotal()+
                "\nVendedor: "+ListaElemen.get(holder.getAdapterPosition()).getVendedor()+
                        "\nHora: "+ListaElemen.get(holder.getAdapterPosition()).getHora_venta()
        );
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
});


        return holder;

    }

    @Override
    public void onBindViewHolder(AdaptadorRegistro_Ventas.adaptadorHolder holder, int position) {
        try {
            holder.txtTitulo.setText(ListaElemen.get(position).getTitulo().toString());
            holder.txtAutor.setText(ListaElemen.get(position).getAutor().toString());
            holder.txtTotal.setText("Total: $"+ListaElemen.get(position).getTotal().toString());
            holder.txtVendidos.setText("Vendidos: "+ListaElemen.get(position).getVendidos().toString());
            if (ListaElemen.get(position).getImagen() != null) {
                holder.imagen.setImageBitmap(ListaElemen.get(position).getImagen());
            } else {
                holder.imagen.setImageResource(R.drawable.bookicon);
            }
            Log.i("AQUI",ListaElemen.get(position).getDato());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ListaElemen.size();
    }
        public class adaptadorHolder extends RecyclerView.ViewHolder{
            TextView txtTitulo,txtAutor,txtVendidos,txtTotal;
            ImageView imagen;
            LinearLayout contenedor;
        public adaptadorHolder(View itemView) {
            super(itemView);
            contenedor=(LinearLayout) itemView.findViewById(R.id.contenedor);
            txtTitulo= (TextView) itemView.findViewById(R.id.txtTitulo);
            txtAutor= (TextView) itemView.findViewById(R.id.txtAutor);
            txtVendidos= (TextView) itemView.findViewById(R.id.txtVendidos);
            imagen=(ImageView) itemView.findViewById(R.id.Imagenv);
            txtTotal=(TextView) itemView.findViewById(R.id.txtTotal);
        }
    }
}
