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
    private CitaAdapter proximasAdapter, pasadasAdapter;
    private CitaAdapter.OnItemClickListener clickListener;
    private CitaAdapter.OnItemLongClickListener longListener;

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

        rvProximas = view.findViewById(R.id.rvCitasProximas);
        rvPasadas  = view.findViewById(R.id.rvCitasPasadas);
        rvProximas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPasadas .setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializar listeners
        clickListener = cita -> {
            Intent intent = new Intent(requireContext(), CitaFormActivity.class);
            intent.putExtra("EXTRA_ID_CITA", cita.getIdCita());
            startActivity(intent);
        };
        longListener = cita -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar cita de “" + cita.getMotivo() + "”?" )
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .citaDao().eliminarCita(cita);
                            requireActivity().runOnUiThread(() -> cargarListas(clickListener, longListener));
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        };

        // Botón para agregar nueva cita
        view.findViewById(R.id.fabAgregarCita)
                .setOnClickListener(v -> startActivity(new Intent(requireContext(), CitaFormActivity.class)));

        // Cargar listas inicialmente
        cargarListas(clickListener, longListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refrescar al volver al fragment
        cargarListas(clickListener, longListener);
    }

    // Método corregido con parámetros para los listeners
    private void cargarListas(CitaAdapter.OnItemClickListener clickListener,
                              CitaAdapter.OnItemLongClickListener longListener) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.obtenerInstancia(requireContext());
            long ahora = System.currentTimeMillis();

            List<Cita> proximas = db.citaDao().obtenerCitasProximas(ahora);
            List<Cita> pasadas  = db.citaDao().obtenerCitasPasadas(ahora);
            List<Doctor> listaDoctores   = db.doctorDao().obtenerDoctores();
            List<Paciente> listaPacientes = db.pacienteDao().obtenerPacientes();

            requireActivity().runOnUiThread(() -> {
                proximasAdapter = new CitaAdapter(proximas, listaDoctores, listaPacientes);
                pasadasAdapter  = new CitaAdapter(pasadas,  listaDoctores, listaPacientes);

                proximasAdapter.setOnItemClickListener(clickListener);
                proximasAdapter.setOnItemLongClickListener(longListener);
                pasadasAdapter .setOnItemClickListener(clickListener);
                pasadasAdapter .setOnItemLongClickListener(longListener);

                rvProximas.setAdapter(proximasAdapter);
                rvPasadas .setAdapter(pasadasAdapter);
            });
        }).start();
    }
}
