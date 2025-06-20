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

    private List<Paciente> listaPacientes = new ArrayList<>(); // lista de pacientes
    private OnItemClickListener clickListener; // para editar
    private OnItemLongClickListener longClickListener; // para eliminar

    public interface OnItemClickListener {
        void onItemClick(Paciente paciente);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Paciente paciente);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    //Permite asignar o actualizar la lista de pacientes
    public void setListaPacientes(List<Paciente> pacientes) {
        listaPacientes = pacientes;
        notifyDataSetChanged();
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
        Paciente p = listaPacientes.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvContacto.setText(p.getDatosContacto());

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

    @Override
    public int getItemCount() {
        return listaPacientes.size();
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvNombre, tvContacto;

        PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar  = itemView.findViewById(R.id.imgPacienteAvatar);
            tvNombre   = itemView.findViewById(R.id.txtNombrePaciente);
            tvContacto = itemView.findViewById(R.id.txtContacto);
        }
    }

}
