package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Paciente;
import androidx.appcompat.app.AlertDialog;

import java.util.List;


public class PacientesFragment extends Fragment{
    private RecyclerView rvPacientes;
    private AdapterPaciente adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_pacientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvPacientes = view.findViewById(R.id.listPacientes);
        adapter = new AdapterPaciente();
        rvPacientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPacientes.setAdapter(adapter);


        adapter.setOnItemClickListener(paciente -> {
            Intent intent = new Intent(requireContext(), FormPacienteActivity.class);
            intent.putExtra("EXTRA_ID_PACIENTE", paciente.getIdPaciente());
            startActivity(intent);
        });


        adapter.setOnItemLongClickListener(paciente -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar al paciente “" + paciente.getNombre() + "”?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .dao_paciente()
                                    .eliminarPaciente(paciente);
                            requireActivity().runOnUiThread(this::cargarPacientes);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });


        view.findViewById(R.id.btnAgregarPacientes).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), FormPacienteActivity.class));
        });


        cargarPacientes();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPacientes();
    }


    private void cargarPacientes() {
        new Thread(() -> {
            List<Paciente> lista = AppDatabase
                    .getInstance(requireContext())
                    .dao_paciente()
                    .obtenerPacientes();
            requireActivity().runOnUiThread(() -> adapter.setListaPacientes(lista));
        }).start();
    }

}
