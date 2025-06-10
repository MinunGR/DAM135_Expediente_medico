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
import com.example.login.expediente_medico.data.Especialidad;

import java.util.List;


public class EspecialidadesFragment extends Fragment{

    private RecyclerView rvEspecialidades;
    private EspecialidadAdapter adapter;

    @Nullable @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_especialidades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView y su adaptador
        rvEspecialidades = view.findViewById(R.id.rvEspecialidades);
        adapter = new EspecialidadAdapter();
        rvEspecialidades.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvEspecialidades.setAdapter(adapter);

        // Al hacer click en una especialidad, se abre el formulario para editar
        adapter.setOnItemClickListener(e -> {
            Intent intent = new Intent(requireContext(), EspecialidadFormActivity.class);
            intent.putExtra("EXTRA_ID_ESPECIALIDAD", e.getIdEspecialidad());
            startActivity(intent);
        });

        // Click largo para eliminar un doctor
        adapter.setOnItemLongClickListener(e -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar “" + e.getNombre() + "”?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .especialidadDao()
                                    .eliminarEspecialidad(e);
                            requireActivity().runOnUiThread(this::cargarEspecialidades);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Abre DoctorFormActivity al pulsar el icono
        view.findViewById(R.id.fabAgregarEspecialidad)
                .setOnClickListener(v ->
                        startActivity(new Intent(requireContext(), EspecialidadFormActivity.class))
                );

        // Carga inicial
        cargarEspecialidades();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarEspecialidades(); // Refreca la lista al volver al formulario
    }

    /* Carga la lista de especialidades desde la BD */
    private void cargarEspecialidades() {
        new Thread(() -> {
            List<Especialidad> lista = AppDatabase
                    .obtenerInstancia(requireContext())
                    .especialidadDao()
                    .obtenerEspecialidades();
            requireActivity().runOnUiThread(() -> adapter.setLista(lista));
        }).start();
    }


}
