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

// Fragmento que muestra el listado de pacientes y un botón para agregar nuevos
public class PacientesFragment extends Fragment{
    private RecyclerView rvPacientes;
    private PacienteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_pacientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView y su adaptador
        rvPacientes = view.findViewById(R.id.rvPacientes);
        adapter = new PacienteAdapter();
        rvPacientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPacientes.setAdapter(adapter);

        // Al hacer click en un paciente, se abre el formulario para editar
        adapter.setOnItemClickListener(paciente -> {
            Intent intent = new Intent(requireContext(), PacienteFormActivity.class);
            intent.putExtra("EXTRA_ID_PACIENTE", paciente.getIdPaciente());
            startActivity(intent);
        });

        // Click largo para eliminar un paciente
        adapter.setOnItemLongClickListener(paciente -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar al paciente “" + paciente.getNombre() + "”?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .pacienteDao()
                                    .eliminarPaciente(paciente);
                            requireActivity().runOnUiThread(this::cargarPacientes);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Abre PacienteFormActivity al pulsar el icono
        view.findViewById(R.id.fabAgregarPaciente).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), PacienteFormActivity.class));
        });

        // 5) Carga inicial de datos
        cargarPacientes();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPacientes(); // Refreca la lista al volver al formulario
    }

    /* Carga la lista de pacientes desde la BD */
    private void cargarPacientes() {
        new Thread(() -> {
            List<Paciente> lista = AppDatabase
                    .obtenerInstancia(requireContext())
                    .pacienteDao()
                    .obtenerPacientes();
            requireActivity().runOnUiThread(() -> adapter.setListaPacientes(lista));
        }).start();
    }

}
