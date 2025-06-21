package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Cita;
import com.example.login.expediente_medico.data.Doctor;
import com.example.login.expediente_medico.data.Paciente;

import java.util.List;

public class CitasFragment extends Fragment {

    private RecyclerView rvProximas, rvPasadas;
    private AdapterCita proximasAdapter, pasadasAdapter;
    private AdapterCita.OnItemClickListener clickListener;
    private AdapterCita.OnItemLongClickListener longListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_citas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProximas = view.findViewById(R.id.listCitasPendientes);
        rvPasadas  = view.findViewById(R.id.listCitasVencidas);
        rvProximas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPasadas .setLayoutManager(new LinearLayoutManager(requireContext()));


        clickListener = cita -> {
            Intent intent = new Intent(requireContext(), FormCitaActivity.class);
            intent.putExtra("EXTRA_ID_CITA", cita.getIdCita());
            startActivity(intent);
        };
        longListener = cita -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar borrado")
                    .setMessage("¿Borrar cita de “" + cita.getMotivo() + "”?" )
                    .setPositiveButton("borrar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .dao_cita().eliminarCita(cita);
                            requireActivity().runOnUiThread(() -> LoadList(clickListener, longListener));
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        };


        view.findViewById(R.id.btnAgregarCita)
                .setOnClickListener(v -> startActivity(new Intent(requireContext(), FormCitaActivity.class)));


        LoadList(clickListener, longListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        LoadList(clickListener, longListener);
    }

    private void LoadList(AdapterCita.OnItemClickListener clickListener,
                          AdapterCita.OnItemLongClickListener longListener) {
        new Thread(() -> {
            AppDatabase database = AppDatabase.getInstance(requireContext());
            long tiempoActual = System.currentTimeMillis();

            List<Doctor> medicos = database.dao_doctor().obtenerDoctores();
            List<Paciente> pacientes = database.dao_paciente().obtenerPacientes();
            List<Cita> citasFuturas = database.dao_cita().listarFuturasOrdenadas(tiempoActual);
            List<Cita> citasAnteriores = database.dao_cita().listarHistoricasRecientes(tiempoActual);

            requireActivity().runOnUiThread(() -> {
                pasadasAdapter = new AdapterCita(citasAnteriores, medicos, pacientes);
                proximasAdapter = new AdapterCita(citasFuturas, medicos, pacientes);

                pasadasAdapter.setOnItemClickListener(clickListener);
                pasadasAdapter.setOnItemLongClickListener(longListener);
                proximasAdapter.setOnItemClickListener(clickListener);
                proximasAdapter.setOnItemLongClickListener(longListener);

                rvPasadas.setAdapter(pasadasAdapter);
                rvProximas.setAdapter(proximasAdapter);
            });
        }).start();
    }

}
