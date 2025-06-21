package com.example.login.expediente_medico.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.RegistroMedico;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterRegistroMedico extends RecyclerView.Adapter<AdapterRegistroMedico.RegistroViewHolder> {
    public AdapterRegistroMedico(){}

    private List<RegistroMedico> lstRegistros;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(RegistroMedico registro);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RegistroMedico registro);
    }

    public void setListaRegistros(List<RegistroMedico> registros) {
        this.lstRegistros = registros;
        notifyDataSetChanged();
    }
    public AdapterRegistroMedico(List<RegistroMedico> registros) {
        this.lstRegistros = registros;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    @NonNull @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_registro, parent, false);
        return new RegistroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
        RegistroMedico r = lstRegistros.get(position);

        Date date = new Date(r.getFechaRegistro());
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(date);
        holder.txtvFecha.setText(fecha);

        holder.txtvDiagnostico.setText("Diagnóstico: " + r.getDiagnostico());
        holder.txtvTratamiento.setText("Tratamiento: " + r.getTratamiento());
        holder.txtvNotas.setText("Notas: " + r.getNotas());

        String uri = r.getFotoUri();
        if (uri != null && !uri.isEmpty()) {
            holder.imgRegistro.setVisibility(View.VISIBLE);
            holder.imgRegistro.setImageURI(Uri.parse(uri));
        } else {
            holder.imgRegistro.setVisibility(View.GONE);
        }

        // Solo click editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(r);
        });

        // Mantener eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(r);
                return true;
            }
            return false;
        });
    }

    static class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView txtvFecha, txtvDiagnostico, txtvTratamiento, txtvNotas;
        ImageView imgRegistro;

        RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvFecha        = itemView.findViewById(R.id.txtFechaRegistro);
            txtvDiagnostico  = itemView.findViewById(R.id.txtDiag);
            txtvTratamiento  = itemView.findViewById(R.id.txtIndicaciones);
            txtvNotas        = itemView.findViewById(R.id.txtNotasExtras);
        }
    }

    @Override
    public int getItemCount() {
        return lstRegistros != null ? lstRegistros.size() : 0;
    }
}
