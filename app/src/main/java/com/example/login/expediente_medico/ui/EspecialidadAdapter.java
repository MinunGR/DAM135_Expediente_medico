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


public class EspecialidadAdapter extends RecyclerView.Adapter<EspecialidadAdapter.EspecialidadViewHolder>{

    private List<Especialidad> lista = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;


    public interface OnItemClickListener {
        void onItemClick(Especialidad especialidad);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Especialidad especialidad);
    }


    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }


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


        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(e);
            }
        });

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


    static class EspecialidadViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;

        public EspecialidadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.txtDescEspecialidad);
        }
    }


}
