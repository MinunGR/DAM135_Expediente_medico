package com.example.login.expediente_medico.ui;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.Doctor;

import java.util.ArrayList;
import java.util.List;

// Muestra la lista de doctores en un Recyclerview
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>{

    // lista
    private List<Doctor> listaDoctores = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }

    //Metodo para registrar el listener
    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    //Permite asignar o actualizar la lista de doctores
    public void setListaDoctores(List<Doctor> doctores) {
        listaDoctores = doctores;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = listaDoctores.get(position);
        holder.tvNombre.setText(doctor.getNombre());
        holder.tvEspecialidad.setText(doctor.getEspecialidad());

        // Si tienes URI de foto, la cargas; si no, permanece el icono por defecto
        String uriFoto = doctor.getFotoUri();
        if (uriFoto != null && !uriFoto.isEmpty()) {
            holder.imgAvatar.setImageURI(Uri.parse(uriFoto));
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_baseline_person_24); // cambiar
        }

        // Click sobre el ítem
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDoctores.size();
    }

    /* ViewHolder interno para un ítem de doctor */
    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNombre, tvEspecialidad;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar       = itemView.findViewById(R.id.imgDoctorAvatar);
            tvNombre        = itemView.findViewById(R.id.tvNombreDoctor);
            tvEspecialidad  = itemView.findViewById(R.id.tvEspecialidadDoctor);
        }
    }

}
