package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Paciente;
import com.example.login.expediente_medico.data.RegistroMedico;

import java.util.List;

// Fragmento que muestra el listado de expediente y un botón para agregar nuevos
public class ExpedienteFragment extends Fragment {

    private Spinner spinnerPacientes;
    private RecyclerView rvHistorial;
    private RegistroMedicoAdapter adapter;
    private List<Paciente> listaPacientes;
    private int pacienteSeleccionadoId = -1;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_expediente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerPacientes = view.findViewById(R.id.spinnerPacientesHistorial);
        rvHistorial      = view.findViewById(R.id.rvHistorial);

        adapter = new RegistroMedicoAdapter(null);
        rvHistorial.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistorial.setAdapter(adapter);

        // Inicializar RecyclerView y su adaptador
        new Thread(() -> {
            listaPacientes = AppDatabase
                    .obtenerInstancia(requireContext())
                    .pacienteDao()
                    .obtenerPacientes();
            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<Paciente> spinnerAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listaPacientes
                );
                spinnerAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item
                );
                spinnerPacientes.setAdapter(spinnerAdapter);
            });
        }).start();

        spinnerPacientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                pacienteSeleccionadoId = listaPacientes.get(pos).getIdPaciente();
                cargarHistorial(pacienteSeleccionadoId);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Al hacer click en un paciente, se abre el formulario para editar
        adapter.setOnItemClickListener(registro -> {
            Intent intent = new Intent(requireContext(), RegistroFormActivity.class);
            intent.putExtra("EXTRA_ID_REGISTRO", registro.getIdRegistro());
            intent.putExtra("EXTRA_ID_PACIENTE", pacienteSeleccionadoId);
            startActivity(intent);
        });

        // Click largo para eliminar un paciente
        adapter.setOnItemLongClickListener(registro -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar este registro médico?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .registroMedicoDao()
                                    .eliminarRegistro(registro);
                            requireActivity().runOnUiThread(() ->
                                    cargarHistorial(pacienteSeleccionadoId)
                            );
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Abre al pulsar el icono
        view.findViewById(R.id.fabAgregarRegistro).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RegistroFormActivity.class);
            intent.putExtra("EXTRA_ID_PACIENTE", pacienteSeleccionadoId);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (pacienteSeleccionadoId != -1) {
            cargarHistorial(pacienteSeleccionadoId);
        }
    }

    private void cargarHistorial(int pacienteId) {
        new Thread(() -> {
            List<RegistroMedico> lista = AppDatabase
                    .obtenerInstancia(requireContext())
                    .registroMedicoDao()
                    .obtenerRegistrosPorPaciente(pacienteId);
            requireActivity().runOnUiThread(() -> adapter.setListaRegistros(lista));
        }).start();
    }
}
