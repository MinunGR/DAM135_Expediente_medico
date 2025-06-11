package com.example.login.expediente_medico.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.Cita;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Muestra la lista de doctores en un Recyclerview
public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private List<Cita> listaCitas; // lista
    private OnItemClickListener clickListener; // editar
    private OnItemLongClickListener longClickListener; // eliminar

    // actualizar
    public interface OnItemClickListener {
        void onItemClick(Cita cita);
    }

    // Eliminar
    public interface OnItemLongClickListener {
        void onItemLongClick(Cita cita);
    }

    public CitaAdapter(List<Cita> citas) {
        this.listaCitas = citas;
    }

    //Metodos para registrar el listener
    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    //Permite asignar o actualizar la lista de consultorios
    public void setListaCitas(List<Cita> citas) {
        this.listaCitas = citas;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(v);
    }

    // Click simple para editar
    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);

        holder.tvPaciente.setText("Paciente: " + cita.getPacienteId());
        holder.tvDoctor.setText("Doctor: " + cita.getDoctorId());

        // Formatear fecha/hora
        Date date = new Date(cita.getFechaHora());
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(date);
        holder.tvFechaHora.setText("Fecha/Hora: " + fechaHora);

        holder.tvMotivo.setText("Motivo: " + cita.getMotivo());

        // Click simple para editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(cita);
        });

        // Click largo para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(cita);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return listaCitas != null ? listaCitas.size() : 0;
    }

    /* ViewHolder interno para un ítem de una cita */
    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaciente, tvDoctor, tvFechaHora, tvMotivo;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaciente  = itemView.findViewById(R.id.tvPacienteCita);
            tvDoctor    = itemView.findViewById(R.id.tvDoctorCita);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHoraCita);
            tvMotivo    = itemView.findViewById(R.id.tvMotivoCita);
        }
    }
}
