package xyz.libresoft.libresoft_app.Adaptadores;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import xyz.libresoft.libresoft_app.DatosLugar;
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.R;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

/**
 * Created by azulm on 21/04/2018.
 */

public class AdaptadorBuscarLugares extends RecyclerView.Adapter<AdaptadorBuscarLugares.adaptadorHolder>{

    List<ElemenBusarLugares> ListaElemen;

    public AdaptadorBuscarLugares(List<ElemenBusarLugares> listaElemen) {
        this.ListaElemen = listaElemen;
    }

    @Override
    public adaptadorHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    /*    View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementobuscarlugares,parent,false);
        final adaptadorHolder holder = new adaptadorHolder(v);

holder.contenedor.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(!Conexion.isConnected(parent.getContext())){
            Toast.makeText(parent.getContext(),"No hay internet para realizar esta acción.",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
           Intent i = new Intent(parent.getContext(), DatosLugar.class);
            i.putExtra("NombreLugar", ListaElemen.get(holder.getAdapterPosition()).getNombreLugar());
            i.putExtra("Disponibles", ListaElemen.get(holder.getAdapterPosition()).getDisponibles());
            i.putExtra("Precio", ListaElemen.get(holder.getAdapterPosition()).getPrecio());
            i.putExtra("Direccion", ListaElemen.get(holder.getAdapterPosition()).getDireccion());
            i.putExtra("Tipo", ListaElemen.get(holder.getAdapterPosition()).getTipo());
            i.putExtra("Correo", ListaElemen.get(holder.getAdapterPosition()).getCorreo());
            i.putExtra("Cuidad", ListaElemen.get(holder.getAdapterPosition()).getCiudad());
            i.putExtra("Estado", ListaElemen.get(holder.getAdapterPosition()).getEstado());
            i.putExtra("Telefono", ListaElemen.get(holder.getAdapterPosition()).getTelefono());
            i.putExtra("Eslogan", ListaElemen.get(holder.getAdapterPosition()).getEslogan());
            i.putExtra("Latitud", ListaElemen.get(holder.getAdapterPosition()).getLatitud());
            i.putExtra("Longuitud", ListaElemen.get(holder.getAdapterPosition()).getLonguitud());
            i.putExtra("Disponibles", ListaElemen.get(holder.getAdapterPosition()).getDisponibles());
            i.putExtra("Oferta", ListaElemen.get(holder.getAdapterPosition()).getOferta());
            i.putExtra("Titulo", ListaElemen.get(holder.getAdapterPosition()).getTitulo());
            i.putExtra("bd", ListaElemen.get(holder.getAdapterPosition()).getBd());
            parent.getContext().startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
});

holder.contenedor.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {

        TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
        SQLiteDatabase db = cnx.getWritableDatabase();
        Cursor datos = db.rawQuery(
                "SELECT * FROM Lugares_Favoritos WHERE codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario + "' and nombre_bd = '" + ListaElemen.get(holder.getAdapterPosition()).getBd()+"'", null
        );
        String a="Añadir a mis lugares";
        if (datos.moveToFirst()) {
            a = "Eliminar de mis lugares";
        }



        final CharSequence[] opciones={a};
        final AlertDialog.Builder builder=new AlertDialog.Builder(parent.getContext());
        builder.setTitle(ListaElemen.get(holder.getAdapterPosition()).getNombreLugar());
        builder.setIcon(R.drawable.ic_action_guar);
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Añadir a mis lugares")){
                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    ContentValues datos = new ContentValues();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String fecha = dateFormat.format(date);
                    datos.put("codigo_usuario", DatosUsuarioLocal.Codigo_Usuario);
                    datos.put("fecha", fecha);
                    datos.put("nombre_bd", ListaElemen.get(holder.getAdapterPosition()).getBd());
                    db.insert("Lugares_Favoritos", null, datos);
                    db.close();
                    Toast.makeText(parent.getContext(),"Se ha agregado",Toast.LENGTH_LONG).show();
                }else{
                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    db.delete("Lugares_Favoritos", "codigo_usuario = '" + DatosUsuarioLocal.Codigo_Usuario + "' and nombre_bd = '" + ListaElemen.get(holder.getAdapterPosition()).getBd()+"'", null);
                    Toast.makeText(parent.getContext(), "Se ha eliminado de tus favoritos.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();


        return false;
    }
});
        return holder;

    }

    @Override
    public void onBindViewHolder(AdaptadorBuscarLugares.adaptadorHolder holder, int position) {
        try{
        holder.txtNombre.setText(ListaElemen.get(position).getNombreLugar().toString());
        holder.txtDisponibles.setText("Disponibles: "+ListaElemen.get(position).getDisponibles().toString());
        if(!ListaElemen.get(position).getOferta().toString().equals("")){
            holder.txtPrecio.setText("$"+ListaElemen.get(position).getPrecio().toString()+" \n-"+ListaElemen.get(position).getOferta().toString());
        }else {
            holder.txtPrecio.setText("$" + ListaElemen.get(position).getPrecio().toString());
        }
        holder.txtDireccion.setText(ListaElemen.get(position).getDireccion().toString());
        if(ListaElemen.get(position).getTipo().toString().equalsIgnoreCase("Biblioteca")){
            holder.imagen.setImageResource(R.drawable.bibliotecas);
            holder.txtPrecio.setText("Consulta Libre");
            }else {
            holder.imagen.setImageResource(R.drawable.librerias);
        }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {
        return ListaElemen.size();
    }
        public class adaptadorHolder extends RecyclerView.ViewHolder{
            TextView txtNombre,txtDisponibles,txtPrecio,txtDireccion;
            ImageView imagen;
            LinearLayout contenedor;
        public adaptadorHolder(View itemView) {

            super(itemView);
            try{
            imagen=(ImageView) itemView.findViewById(R.id.ImagenLugar);
            contenedor=(LinearLayout) itemView.findViewById(R.id.contenedorlugares);
            txtNombre= (TextView) itemView.findViewById(R.id.txtNombre);
            txtDisponibles= (TextView) itemView.findViewById(R.id.txtDisponibles);
            txtPrecio= (TextView) itemView.findViewById(R.id.txtPrecio);
            txtDireccion=(TextView) itemView.findViewById(R.id.txtDireccion);
        } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(itemView.getContext(), e.getMessage() +
                        " ", Toast.LENGTH_LONG).show();
            }
        }
    }
























}
