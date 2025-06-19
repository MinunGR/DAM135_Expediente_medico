package com.example.login.expediente_medico.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.Cita;
import com.example.login.expediente_medico.data.Doctor;
import com.example.login.expediente_medico.data.Paciente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Muestra la lista de doctores en un Recyclerview
public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private List<Cita> listaCitas;
    private List<Doctor> listaDoctores;
    private List<Paciente> listaPacientes;
    OnItemClickListener clickListener;
    OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Cita cita);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Cita cita);
    }

    // Nuevo constructor recibe también doctores y pacientes
    public CitaAdapter(List<Cita> citas,
                       List<Doctor> doctores,
                       List<Paciente> pacientes) {
        this.listaCitas     = citas;
        this.listaDoctores  = doctores;
        this.listaPacientes = pacientes;
    }

    // Permite actualizar lista de citas
    public void setListaCitas(List<Cita> citas) {
        this.listaCitas = citas;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        this.longClickListener = l;
    }

    @NonNull @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);

        // Buscar nombre de paciente
        String nombrePac = "Desconocido";
        for (Paciente p : listaPacientes) {
            if (p.getIdPaciente() == cita.getPacienteId()) {
                nombrePac = p.getNombre();
                break;
            }
        }
        holder.tvPaciente.setText("Paciente: " + nombrePac);

        // Buscar nombre de doctor
        String nombreDoc = "Desconocido";
        for (Doctor d : listaDoctores) {
            if (d.getIdDoctor() == cita.getDoctorId()) {
                nombreDoc = d.getNombre();
                break;
            }
        }
        holder.tvDoctor.setText("Doctor: " + nombreDoc);

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
