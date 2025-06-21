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
import com.example.login.expediente_medico.data.Doctor;

import java.util.ArrayList;
import java.util.List;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.DoctorViewHolder>{
    private List<Doctor> lstDoctores = new ArrayList<>();
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Doctor doctor);
    }

    public  AdapterDoctor(){}

    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    public void setListaDoctores(List<Doctor> doctores) {
        lstDoctores = doctores;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lstDoctores.size();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_doctor, parent, false);
        return new DoctorViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = lstDoctores.get(position);
        holder.txtvNombre.setText(doctor.getNombre());
        holder.txtvEspecialidad.setText(doctor.getEspecialidad());

        String uriFoto = doctor.getFotoUri();
        if (uriFoto != null && !uriFoto.isEmpty()) {
            holder.imgAvatar.setImageURI(Uri.parse(uriFoto));
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_baseline_person_24);
        }

        // Solo click editar
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(doctor);
            }
        });

        // Mantener eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(doctor);
                return true;
            }
            return false;
        });
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtvNombre,
                txtvEspecialidad;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar       = itemView.findViewById(R.id.imgDoctorAvatar);
            txtvNombre        = itemView.findViewById(R.id.txtNombreDoctor);
            txtvEspecialidad  = itemView.findViewById(R.id.txtEspecialidad);
        }
    }

}
