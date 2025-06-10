package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Especialidad;

public class EspecialidadFormActivity extends AppCompatActivity {

    private EditText etNombre;
    private Button btnGuardar;
    private boolean esEdicion;
    private int idEspecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_especialidad);

        // enlazar views
        etNombre     = findViewById(R.id.etNombreEspecialidad);
        btnGuardar   = findViewById(R.id.btnGuardarEspecialidad);

        // Detectamos si es creación o edición
        idEspecialidad = getIntent().getIntExtra("EXTRA_ID_ESPECIALIDAD", -1);
        esEdicion = idEspecialidad != -1;
        if (esEdicion) {
            setTitle("Editar Especialidad");
            // Carga datos en hilo de fondo
            new Thread(() -> {
                Especialidad e = AppDatabase
                        .obtenerInstancia(this)
                        .especialidadDao()
                        .buscarEspecialidadPorId(idEspecialidad);
                runOnUiThread(() -> {
                    if (e != null) {
                        etNombre.setText(e.getNombre());
                    }
                });
            }).start();
        } else {
            setTitle("Nueva Especialidad");
        }

        // Guardar (insertar o actualizar)
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();

            // Validar campos completados
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            // Guardar en BD en hilo de fondo
            new Thread(() -> {
                AppDatabase db = AppDatabase.obtenerInstancia(this);
                if (esEdicion) {
                    // Actualizar un doctor existente
                    Especialidad e = new Especialidad(nombre);
                    e.setIdEspecialidad(idEspecialidad);
                    db.especialidadDao().actualizarEspecialidad(e);
                } else {
                    // Insertar nuevo
                    db.especialidadDao().insertarEspecialidad(new Especialidad(nombre));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }
}
