package com.example.login.expediente_medico.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.Consultorio;

import java.util.ArrayList;
import java.util.List;

// Muestra la lista de doctores en un Recyclerview
public class ConsultorioAdapter extends RecyclerView.Adapter<ConsultorioAdapter.ConsultorioViewHolder> {

    private List<Consultorio> lista = new ArrayList<>(); // lista
    private OnItemClickListener clickListener; // actualizar
    private OnItemLongClickListener longClickListener; // eliminar

    // actualizar
    public interface OnItemClickListener {
        void onItemClick(Consultorio consultorio);
    }

    // elimianr
    public interface OnItemLongClickListener {
        void onItemLongClick(Consultorio consultorio);
    }

    //Metodos para registrar el listener
    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    //Permite asignar o actualizar la lista de consultorios
    public void setLista(List<Consultorio> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConsultorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consultorio, parent, false);
        return new ConsultorioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultorioViewHolder holder, int position) {
        Consultorio c = lista.get(position);
        holder.tvNombre.setText(c.getNombre());
        holder.tvUbicacion.setText(c.getUbicacion());

        // Click simple para editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(c);
            }
        });

        // Click largo para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(c);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    /* ViewHolder interno para un ítem de doctor */
    static class ConsultorioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvUbicacion;

        ConsultorioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre     = itemView.findViewById(R.id.tvNombreConsultorio);
            tvUbicacion  = itemView.findViewById(R.id.tvUbicacionConsultorio);
        }
    }
}
