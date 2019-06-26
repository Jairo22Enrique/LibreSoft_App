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
import xyz.libresoft.libresoft_app.Entidades.DatosUsuarioLocal;
import xyz.libresoft.libresoft_app.Entidades.Elemen;
import xyz.libresoft.libresoft_app.R;
import xyz.libresoft.libresoft_app.SQLite.TablaUsuariosDatos;

/**
 * Created by azulm on 21/04/2018.
 */

public class Adaptador extends RecyclerView.Adapter<Adaptador.adaptadorHolder>{

    List<Elemen> ListaElemen;

    public Adaptador(List<Elemen> listaElemen) {
        this.ListaElemen = listaElemen;
    }

    @Override
    public adaptadorHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    /*    View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento,parent,false);
        final adaptadorHolder holder = new adaptadorHolder(v);

holder.contenedor.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(!Conexion.isConnected(parent.getContext())){
            Toast.makeText(parent.getContext(),"No hay internet para realizar esta acción.",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent i = new Intent(parent.getContext(), DatosLibros.class);
            i.putExtra("Titulo", ListaElemen.get(holder.getAdapterPosition()).getTitulo());
            i.putExtra("Autor", ListaElemen.get(holder.getAdapterPosition()).getAutor());
            i.putExtra("Editorial", ListaElemen.get(holder.getAdapterPosition()).getEditorial());
            i.putExtra("Precio", ListaElemen.get(holder.getAdapterPosition()).getPrecio());
            i.putExtra("Lugar", ListaElemen.get(holder.getAdapterPosition()).getLugar());
            i.putExtra("Genero", ListaElemen.get(holder.getAdapterPosition()).getGenero());
            i.putExtra("Año", ListaElemen.get(holder.getAdapterPosition()).getAno());
            i.putExtra("Edicion", ListaElemen.get(holder.getAdapterPosition()).getEdicion());
            i.putExtra("Descripcion", ListaElemen.get(holder.getAdapterPosition()).getDescipcion());
            i.putExtra("Imagen", ListaElemen.get(holder.getAdapterPosition()).getDato());
            i.putExtra("Codigo", ListaElemen.get(holder.getAdapterPosition()).getCodigo());
            i.putExtra("Estrellas", ListaElemen.get(holder.getAdapterPosition()).getEstrellas());
            //Extras
            i.putExtra("Estante", ListaElemen.get(holder.getAdapterPosition()).getEstante());
            i.putExtra("BuscandoEn", ListaElemen.get(holder.getAdapterPosition()).getBuscandoEn());
            i.putExtra("Unidades", ListaElemen.get(holder.getAdapterPosition()).getUnidades());
            i.putExtra("Oferta", "");
            parent.getContext().startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
            Intent i = new Intent(parent.getContext(), DatosLibros.class);
            Toast.makeText(parent.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            i.putExtra("Titulo", ListaElemen.get(holder.getAdapterPosition()).getTitulo());
            i.putExtra("Autor", ListaElemen.get(holder.getAdapterPosition()).getAutor());
            i.putExtra("Editorial", ListaElemen.get(holder.getAdapterPosition()).getEditorial());
            i.putExtra("Precio", ListaElemen.get(holder.getAdapterPosition()).getPrecio());
            i.putExtra("Año", ListaElemen.get(holder.getAdapterPosition()).getAno());
            i.putExtra("Edicion", ListaElemen.get(holder.getAdapterPosition()).getEdicion());
            i.putExtra("Lugar", ListaElemen.get(holder.getAdapterPosition()).getLugar());
            i.putExtra("Descripcion", ListaElemen.get(holder.getAdapterPosition()).getDescipcion());
            i.putExtra("Genero", ListaElemen.get(holder.getAdapterPosition()).getGenero());
            i.putExtra("Codigo", ListaElemen.get(holder.getAdapterPosition()).getCodigo());
            i.putExtra("Estrellas", ListaElemen.get(holder.getAdapterPosition()).getEstrellas());
            //
            i.putExtra("Estante", ListaElemen.get(holder.getAdapterPosition()).getEstante());
            i.putExtra("BuscandoEn", ListaElemen.get(holder.getAdapterPosition()).getBuscandoEn());
            i.putExtra("Unidades", ListaElemen.get(holder.getAdapterPosition()).getUnidades());
            i.putExtra("Oferta", "");
            parent.getContext().startActivity(i);
        }
    }
});
holder.contenedor.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
        SQLiteDatabase db = cnx.getWritableDatabase();
        Cursor datos = db.rawQuery(
                "SELECT * FROM Guardados WHERE codigo_usuario = '"+DatosUsuarioLocal.Codigo_Usuario+"' and codigo_libro = '"+ListaElemen.get(holder.getAdapterPosition()).getCodigo()+"'", null
        );
        String a="Guardar Libro";
        if (datos.moveToFirst()) {
            a="Eliminar libro de guardados";
        }
        final CharSequence[] opciones={a};
        final AlertDialog.Builder builder=new AlertDialog.Builder(parent.getContext());
        builder.setTitle(ListaElemen.get(holder.getAdapterPosition()).getTitulo());
        builder.setIcon(R.drawable.ic_action_estrella);
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Guardar Libro")){
                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    ContentValues datos = new ContentValues();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String fecha = dateFormat.format(date);
                    datos.put("codigo_usuario", DatosUsuarioLocal.Codigo_Usuario);
                    datos.put("fecha", fecha);
                    datos.put("codigo_libro", ListaElemen.get(holder.getAdapterPosition()).getCodigo());
                    db.insert("Guardados", null, datos);
                    db.close();
                    Toast.makeText(parent.getContext(),"Se ha guardado el libro",Toast.LENGTH_LONG).show();
                }else{
                    TablaUsuariosDatos cnx = new TablaUsuariosDatos(parent.getContext(), "UsuarioDatos", null, 1);
                    SQLiteDatabase db = cnx.getWritableDatabase();
                    db.delete("Guardados", "codigo_usuario = " + DatosUsuarioLocal.Codigo_Usuario + " and codigo_libro = " + ListaElemen.get(holder.getAdapterPosition()).getCodigo(), null);

                    Toast.makeText(parent.getContext(), "Se ha eliminado de tus guardados.", Toast.LENGTH_LONG).show();
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
    public void onBindViewHolder(Adaptador.adaptadorHolder holder, int position) {
        try {
            holder.txtTitulo.setText(ListaElemen.get(position).getTitulo().toString());
            holder.txtAutor.setText(ListaElemen.get(position).getAutor().toString());
            holder.txtGenero.setText(ListaElemen.get(position).getGenero().toString());
            holder.txtRating.setText(ListaElemen.get(position).getEstrellas().toString() + " / 5");
            if (ListaElemen.get(position).getImagen() != null) {
                holder.imagen.setImageBitmap(ListaElemen.get(position).getImagen());
            } else {
                holder.imagen.setImageResource(R.drawable.bookicon);
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
            TextView txtTitulo,txtAutor,txtGenero,txtRating;
            ImageView imagen;
            LinearLayout contenedor;
        public adaptadorHolder(View itemView) {
            super(itemView);
            contenedor=(LinearLayout) itemView.findViewById(R.id.contenedor);
            txtTitulo= (TextView) itemView.findViewById(R.id.txtTitulo);
            txtAutor= (TextView) itemView.findViewById(R.id.txtAutor);
            txtGenero= (TextView) itemView.findViewById(R.id.txtGenero);
            imagen=(ImageView) itemView.findViewById(R.id.Imagen);
            txtRating=(TextView) itemView.findViewById(R.id.txtRating);
        }
    }
}
