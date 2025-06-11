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

import java.util.List;

public class CitasFragment extends Fragment {

    private RecyclerView rvProximas, rvPasadas;
    private CitaAdapter proximasAdapter, pasadasAdapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_citas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView y su adaptador
        rvProximas = view.findViewById(R.id.rvCitasProximas);
        rvPasadas  = view.findViewById(R.id.rvCitasPasadas);

        proximasAdapter = new CitaAdapter(null);
        pasadasAdapter  = new CitaAdapter(null);

        rvProximas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPasadas.setLayoutManager(new LinearLayoutManager(requireContext()));

        rvProximas.setAdapter(proximasAdapter);
        rvPasadas.setAdapter(pasadasAdapter);

        // Configurar clicks (reutiliza ambos adaptadores)
        CitaAdapter.OnItemClickListener clickListener = cita -> {
            Intent intent = new Intent(requireContext(), CitaFormActivity.class);
            intent.putExtra("EXTRA_ID_CITA", cita.getIdCita());
            startActivity(intent);
        };
        CitaAdapter.OnItemLongClickListener longListener = cita -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar cita de “" + cita.getMotivo() + "”?")
                    .setPositiveButton("Eliminar", (d, w) -> {
                        new Thread(() -> {
                            AppDatabase.obtenerInstancia(requireContext())
                                    .citaDao().eliminarCita(cita);
                            requireActivity().runOnUiThread(this::cargarListas);
                        }).start();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        };

        proximasAdapter.setOnItemClickListener(clickListener);
        proximasAdapter.setOnItemLongClickListener(longListener);
        pasadasAdapter.setOnItemClickListener(clickListener);
        pasadasAdapter.setOnItemLongClickListener(longListener);

        // 3) Agregar nueva cita
        view.findViewById(R.id.fabAgregarCita).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CitaFormActivity.class))
        );

        // 4) Cargar ambas listas
        cargarListas();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarListas();
    }

    // refresca la lista
    private void cargarListas() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.obtenerInstancia(requireContext());
            long ahora = System.currentTimeMillis();
            List<Cita> proximas = db.citaDao().obtenerCitasProximas(ahora);
            List<Cita> pasadas  = db.citaDao().obtenerCitasPasadas(ahora);

            requireActivity().runOnUiThread(() -> {
                proximasAdapter.setListaCitas(proximas);
                pasadasAdapter.setListaCitas(pasadas);
            });
        }).start();
    }
}

