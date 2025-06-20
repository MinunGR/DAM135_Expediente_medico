package com.example.login.expediente_medico.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.Especialidad;

import java.util.ArrayList;
import java.util.List;

// Muestra la lista de doctores en un Recyclerview
public class EspecialidadAdapter extends RecyclerView.Adapter<EspecialidadAdapter.EspecialidadViewHolder>{

    private List<Especialidad> lista = new ArrayList<>(); // lista
    private OnItemClickListener clickListener; // editar
    private OnItemLongClickListener longClickListener; // eliminar

    // Interfaces de callbacks
    public interface OnItemClickListener {
        void onItemClick(Especialidad especialidad);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Especialidad especialidad);
    }

    // Registradores de callbacks
    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    // Actualizar la lista de datos
    public void setLista(List<Especialidad> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EspecialidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_especialidad, parent, false);
        return new EspecialidadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EspecialidadViewHolder holder, int position) {
        Especialidad e = lista.get(position);
        holder.tvNombre.setText(e.getNombre());

        // Click simple para editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(e);
            }
        });

        // Click largo para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(e);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    /** ViewHolder interno para item_especialidad.xml */
    static class EspecialidadViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;

        public EspecialidadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txtDescEspecialidad);
        }
    }


}
