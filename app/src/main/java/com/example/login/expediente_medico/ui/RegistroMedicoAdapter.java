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

// Muestra la lista de los registros
public class RegistroMedicoAdapter
        extends RecyclerView.Adapter<RegistroMedicoAdapter.RegistroViewHolder> {

    private List<RegistroMedico> listaRegistros;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    // Actualizar
    public interface OnItemClickListener {
        void onItemClick(RegistroMedico registro);
    }

    // Eliminar
    public interface OnItemLongClickListener {
        void onItemLongClick(RegistroMedico registro);
    }

    public RegistroMedicoAdapter(List<RegistroMedico> registros) {
        this.listaRegistros = registros;
    }

    //Metodos para registrar el listener
    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }


    //Permite asignar o actualizar la lista de consultorios
    public void setListaRegistros(List<RegistroMedico> registros) {
        this.listaRegistros = registros;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public RegistroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registro, parent, false);
        return new RegistroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroViewHolder holder, int position) {
        RegistroMedico r = listaRegistros.get(position);

        // Fecha formateada
        Date date = new Date(r.getFechaRegistro());
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(date);
        holder.tvFecha.setText(fecha);

        holder.tvDiagnostico.setText("Diagnóstico: " + r.getDiagnostico());
        holder.tvTratamiento.setText("Tratamiento: " + r.getTratamiento());
        holder.tvNotas.setText("Notas: " + r.getNotas());

        String uri = r.getFotoUri();
        if (uri != null && !uri.isEmpty()) {
            holder.imgRegistro.setVisibility(View.VISIBLE);
            holder.imgRegistro.setImageURI(Uri.parse(uri));
        } else {
            holder.imgRegistro.setVisibility(View.GONE);
        }

        // Click simple para editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(r);
        });

        // Click largo para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(r);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaRegistros != null ? listaRegistros.size() : 0;
    }

    /* ViewHolder interno para un ítem de doctor */
    static class RegistroViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDiagnostico, tvTratamiento, tvNotas;
        ImageView imgRegistro;

        RegistroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha        = itemView.findViewById(R.id.tvFechaRegistro);
            tvDiagnostico  = itemView.findViewById(R.id.tvDiagnostico);
            tvTratamiento  = itemView.findViewById(R.id.tvTratamiento);
            tvNotas        = itemView.findViewById(R.id.tvNotas);
            imgRegistro    = itemView.findViewById(R.id.imgRegistro);
        }
    }
}
