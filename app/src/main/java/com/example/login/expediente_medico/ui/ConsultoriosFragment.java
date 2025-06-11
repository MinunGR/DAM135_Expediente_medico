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
import com.example.login.expediente_medico.data.Consultorio;

import java.util.List;

public class ConsultoriosFragment extends Fragment {

    private RecyclerView rvConsultorios;
    private ConsultorioAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_consultorios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView y su adaptador
        rvConsultorios = view.findViewById(R.id.rvConsultorios);
        adapter = new ConsultorioAdapter();
        rvConsultorios.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvConsultorios.setAdapter(adapter);

        // Al hacer click en un consultorio, se abre el formulario para editar
        adapter.setOnItemClickListener(c -> {
            Intent intent = new Intent(requireContext(), ConsultorioFormActivity.class);
            intent.putExtra("EXTRA_ID_CONSULTORIO", c.getIdConsultorio());
            startActivity(intent);
        });

        // Click largo para eliminar un consultorio
        adapter.setOnItemLongClickListener(c -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar “" + c.getNombre() + "”?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .consultorioDao()
                                    .eliminarConsultorio(c);
                            // Refrescar la lista en UI thread
                            requireActivity().runOnUiThread(this::cargarConsultorios);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // FAB para añadir
        view.findViewById(R.id.fabAgregarConsultorio).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ConsultorioFormActivity.class))
        );

        // Abre DoctorFormActivity al pulsar el icono
        cargarConsultorios();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarConsultorios(); // Refreca la lista al volver al formulario
    }

    // refresca la lista
    private void cargarConsultorios() {
        new Thread(() -> {
            List<Consultorio> lista = AppDatabase
                    .obtenerInstancia(requireContext())
                    .consultorioDao()
                    .obtenerConsultorios();
            requireActivity().runOnUiThread(() -> adapter.setLista(lista));
        }).start();
    }
}
