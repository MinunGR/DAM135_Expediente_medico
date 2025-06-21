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


public class DoctoresFragment extends Fragment{
    private RecyclerView rvDoctores;
    private AdapterDoctor adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_doctores, container, false);
    }


    private void loadDoctros() {
        new Thread(() -> {
            AppDatabase medicalDB = AppDatabase.getInstance(requireContext());
            List<Doctor> medicos = medicalDB.dao_doctor().obtenerDoctores();

            requireActivity().runOnUiThread(() -> {
                adapter.setListaDoctores(medicos);
            });
        }).start();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvDoctores = view.findViewById(R.id.listDoctores);
        adapter = new AdapterDoctor();
        rvDoctores.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDoctores.setAdapter(adapter);


        adapter.setOnItemClickListener(doctor -> {
            Intent intent = new Intent(requireContext(), FormDoctorActivity.class);
            intent.putExtra("EXTRA_ID_DOCTOR", doctor.getIdDoctor());
            startActivity(intent);
        });


        adapter.setOnItemLongClickListener(doctor -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar Borrado")
                    .setMessage("¿Borrar al doctor “" + doctor.getNombre() + "”?")
                    .setPositiveButton("Borrar", (dialog, which) -> {

                        new Thread(() -> {
                            AppDatabase.getInstance(requireContext())
                                    .dao_doctor()
                                    .eliminarDoctor(doctor);

                            requireActivity().runOnUiThread(this::loadDoctros);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });


        view.findViewById(R.id.btnAgregarDoctor).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FormDoctorActivity.class);
            startActivity(intent);
        });

        loadDoctros();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDoctros();
    }
}
