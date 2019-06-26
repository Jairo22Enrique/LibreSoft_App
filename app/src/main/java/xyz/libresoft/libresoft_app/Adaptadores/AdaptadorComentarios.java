package xyz.libresoft.libresoft_app.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import xyz.libresoft.libresoft_app.Entidades.ElemenBusarLugares;
import xyz.libresoft.libresoft_app.Entidades.ElemenComentarios;
import xyz.libresoft.libresoft_app.R;

/**
 * Created by azulm on 21/04/2018.
 */

public class AdaptadorComentarios extends RecyclerView.Adapter<AdaptadorComentarios.adaptadorHolder>{

    List<ElemenComentarios> ListaElemen;

    public AdaptadorComentarios(List<ElemenComentarios> listaElemen) {
        this.ListaElemen = listaElemen;
    }

    @Override
    public adaptadorHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elementocomentarios,parent,false);
        final adaptadorHolder holder = new adaptadorHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdaptadorComentarios.adaptadorHolder holder, int position) {
        try{
        holder.txtComentario.setText(ListaElemen.get(position).getTituloReseña().toString()+": "+ListaElemen.get(position).getReseña().toString());
        holder.txtUsuarioComentario.setText(ListaElemen.get(position).getNombreUsuario().toString());
            holder.txtFecha.setText(ListaElemen.get(position).getFecha().toString());
            holder.calificacionComentario.setText(ListaElemen.get(position).getEstrellas().toString());
            holder.imagen.setImageResource(R.drawable.usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ListaElemen.size();
    }
        public class adaptadorHolder extends RecyclerView.ViewHolder{
            TextView txtUsuarioComentario,txtComentario, calificacionComentario,txtFecha;
            ImageView imagen;
            LinearLayout contenedor;
        public adaptadorHolder(View itemView) {

            super(itemView);
            try{
            imagen=(ImageView) itemView.findViewById(R.id.ImagenUsuarioComentario);
            contenedor=(LinearLayout) itemView.findViewById(R.id.contenedorcomentarios);
                txtUsuarioComentario= (TextView) itemView.findViewById(R.id.txtUsuarioComentario);
                calificacionComentario= (TextView) itemView.findViewById(R.id.calificacionComentario);
                txtComentario= (TextView) itemView.findViewById(R.id.txtComentario);
                txtFecha= (TextView) itemView.findViewById(R.id.txtFecha);

        } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(itemView.getContext(), e.getMessage() +
                        " ", Toast.LENGTH_LONG).show();
            }
        }
    }
























}
