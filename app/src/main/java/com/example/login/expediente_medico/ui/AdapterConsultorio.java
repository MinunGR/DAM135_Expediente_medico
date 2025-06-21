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

public class AdapterConsultorio extends RecyclerView.Adapter<AdapterConsultorio.ConsultorioViewHolder> {
    private List<Consultorio> lst = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    public void setLista(List<Consultorio> lista) {
        this.lst = lista;
        notifyDataSetChanged();
    }

    public AdapterConsultorio(){}

    public interface OnItemClickListener {
        void onItemClick(Consultorio consultorio);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Consultorio consultorio);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultorioViewHolder holder, int position) {
        Consultorio c = lst.get(position);
        holder.txtvNombre.setText(c.getNombre());
        holder.txtvUbicacion.setText(c.getUbicacion());

        // Solo click editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(c);
            }
        });

        // Mantener eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(c);
                return true;
            }
            return false;
        });
    }

    @NonNull
    @Override
    public ConsultorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_consultorio, parent, false);
        return new ConsultorioViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    static class ConsultorioViewHolder extends RecyclerView.ViewHolder {
        TextView txtvNombre,
                txtvUbicacion;

        ConsultorioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvNombre     = itemView.findViewById(R.id.txtConsultorio);
            txtvUbicacion  = itemView.findViewById(R.id.txtUbicacion);
        }
    }
}
