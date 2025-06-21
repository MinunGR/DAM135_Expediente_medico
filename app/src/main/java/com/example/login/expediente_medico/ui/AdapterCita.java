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

public class AdapterCita extends RecyclerView.Adapter<AdapterCita.CitaViewHolder> {
    private List<Cita> lstCitas;
    private List<Doctor> lstDoctores;
    private List<Paciente> lstPacientes;
    OnItemClickListener clickListener;
    OnItemLongClickListener longClickListener;

    public AdapterCita(List<Cita> citas,
                       List<Doctor> doctores,
                       List<Paciente> pacientes)
    {
        this.lstCitas     = citas;
        this.lstDoctores  = doctores;
        this.lstPacientes = pacientes;
    }

    public  AdapterCita(){}

    public void setLstCitas(List<Cita> citas) {
        this.lstCitas = citas;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Cita cita);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Cita cita);
    }

    @NonNull @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cita, parent, false);
        return new CitaViewHolder(v);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        this.longClickListener = l;
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        String noEncontradoTxt = String.valueOf(R.string.txt_no_encontrado);
        Cita cita = lstCitas.get(position);

        String nombrePac = noEncontradoTxt;
        for (Paciente p : lstPacientes) {
            if (p.getIdPaciente() == cita.getPacienteId()) {
                nombrePac = p.getNombre();
                break;
            }
        }
        holder.txtvPaciente.setText("Paciente: " + nombrePac);

        String nombreDoc = noEncontradoTxt;
        for (Doctor d : lstDoctores) {
            if (d.getIdDoctor() == cita.getDoctorId()) {
                nombreDoc = d.getNombre();
                break;
            }
        }
        holder.txtvDoctor.setText("Doctor: " + nombreDoc);

        Date date = new Date(cita.getFechaHora());
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(date);
        holder.txtvFechaHora.setText("Fecha/Hora: " + fechaHora);

        holder.txtvMotivo.setText("Motivo: " + cita.getMotivo());

        // Solo click editar
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(cita);
        });

        // Mantener eliminar
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
        return lstCitas != null ? lstCitas.size() : 0;
    }

    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView txtvPaciente,
                txtvDoctor,
                txtvFechaHora,
                txtvMotivo;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtvPaciente  = itemView.findViewById(R.id.txtPacienteCita);
            txtvDoctor    = itemView.findViewById(R.id.txtDoctorCita);
            txtvFechaHora = itemView.findViewById(R.id.txtFechaHoraCita);
            txtvMotivo    = itemView.findViewById(R.id.txtMotivoCita);
        }
    }
}
