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


public class ExpedienteFragment extends Fragment {

    private Spinner spinnerPacientes;
    private RecyclerView rvHistorial;
    private AdapterRegistroMedico adapter;
    private List<Paciente> listaPacientes;
    private int pacienteSeleccionadoId = -1;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_expediente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerPacientes = view.findViewById(R.id.slcPacientes);
        rvHistorial      = view.findViewById(R.id.listHistorial);

        adapter = new AdapterRegistroMedico(null);
        rvHistorial.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistorial.setAdapter(adapter);


        new Thread(() -> {
            listaPacientes = AppDatabase
                    .getInstance(requireContext())
                    .dao_paciente()
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
                loadHistori(pacienteSeleccionadoId);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });


        adapter.setOnItemClickListener(registro -> {
            Intent intent = new Intent(requireContext(), FormRegistroActivity.class);
            intent.putExtra("EXTRA_ID_REGISTRO", registro.getIdRegistro());
            intent.putExtra("EXTRA_ID_PACIENTE", pacienteSeleccionadoId);
            startActivity(intent);
        });


        adapter.setOnItemLongClickListener(registro -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.titulo_eliminacion)
                    .setMessage(R.string.confirmar_eliminacion)
                    .setPositiveButton(R.string.button_eliminacion, (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .registroMedicoDao()
                                    .eliminarRegistro(registro);
                            requireActivity().runOnUiThread(() ->
                                    loadHistori(pacienteSeleccionadoId)
                            );
                        }).start();
                    })
                    .setNegativeButton(R.string.button_cancelar, null)
                    .show();
        });


        view.findViewById(R.id.btnAgregarHistorico).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FormRegistroActivity.class);
            intent.putExtra("EXTRA_ID_PACIENTE", pacienteSeleccionadoId);
            startActivity(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (pacienteSeleccionadoId != -1) {
            loadHistori(pacienteSeleccionadoId);
        }
    }

    private void loadHistori(int idPaciente) {
        new Thread(() -> {
            AppDatabase baseDatos = AppDatabase.getInstance(requireContext());
            List<RegistroMedico> historialClinico = baseDatos.registroMedicoDao()
                    .obtenerRegistrosPorPaciente(idPaciente);

            requireActivity().runOnUiThread(() -> {
                adapter.setListaRegistros(historialClinico);
            });
        }).start();
    }
}
