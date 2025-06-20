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
import com.example.login.expediente_medico.data.Doctor;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

// Fragmento que muestra el listado de doctores y un botón para agregar nuevos
public class DoctoresFragment extends Fragment{
    private RecyclerView rvDoctores;
    private DoctorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_doctores, container, false);
    }

    // refresca la lista
    private void cargarDoctores() {
        new Thread(() -> {
            List<Doctor> lista = AppDatabase
                    .obtenerInstancia(requireContext())
                    .doctorDao()
                    .obtenerDoctores();
            requireActivity().runOnUiThread(() -> adapter.setListaDoctores(lista));
        }).start();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView y su adaptador
        rvDoctores = view.findViewById(R.id.rvDoctores);
        adapter = new DoctorAdapter();
        rvDoctores.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDoctores.setAdapter(adapter);

        // Al hacer click en un doctor, se abre el formulario para editar
        adapter.setOnItemClickListener(doctor -> {
            Intent intent = new Intent(requireContext(), DoctorFormActivity.class);
            intent.putExtra("EXTRA_ID_DOCTOR", doctor.getIdDoctor());
            startActivity(intent);
        });

        // Click largo para eliminar un doctor
        adapter.setOnItemLongClickListener(doctor -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar al doctor “" + doctor.getNombre() + "”?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        // Eliminar en BD en hilo de fondo
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .doctorDao()
                                    .eliminarDoctor(doctor);
                            // Refrescar la lista en UI thread
                            requireActivity().runOnUiThread(this::cargarDoctores);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Abre DoctorFormActivity al pulsar el icono
        view.findViewById(R.id.fabAgregarDoctor).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DoctorFormActivity.class);
            startActivity(intent);
        });

        cargarDoctores();

    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDoctores(); // Refreca la lista al volver al formulario
    }
}
