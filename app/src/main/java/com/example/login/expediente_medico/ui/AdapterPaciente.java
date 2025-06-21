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
import com.example.login.expediente_medico.data.Paciente;

import java.util.ArrayList;
import java.util.List;

public class AdapterPaciente extends RecyclerView.Adapter<AdapterPaciente.PacienteViewHolder>{
    private List<Paciente> lstPacientes = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public AdapterPaciente(){}

    public interface OnItemClickListener {
        void onItemClick(Paciente paciente);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Paciente paciente);
    }

    public void setListaPacientes(List<Paciente> pacientes) {
        lstPacientes = pacientes;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_paciente, parent, false);
        return new PacienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Paciente p = lstPacientes.get(position);
        holder.txtvNombre.setText(p.getNombre());
        holder.txtvContacto.setText(p.getDatosContacto());

        String uriFoto = p.getFotoUri();
        if (uriFoto != null && !uriFoto.isEmpty()) {
            holder.imgAvatar.setImageURI(Uri.parse(uriFoto));
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_baseline_person_24);
        }

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(p);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(p);
                return true;
            }
            return false;
        });
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtvNombre,
                txtvContacto;

        PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar  = itemView.findViewById(R.id.imgPacienteAvatar);
            txtvNombre   = itemView.findViewById(R.id.txtNombrePaciente);
            txtvContacto = itemView.findViewById(R.id.txtContacto);
        }
    }

    @Override
    public int getItemCount() {
        return lstPacientes.size();
    }

}
