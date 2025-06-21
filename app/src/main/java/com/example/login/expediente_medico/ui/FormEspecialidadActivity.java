package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Especialidad;

public class FormEspecialidadActivity extends AppCompatActivity {

    private EditText etNombre;
    private Button btnGuardar;
    private boolean esEdicion;
    private int idEspecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_especialidad);


        etNombre     = findViewById(R.id.etNombreEspecialidad);
        btnGuardar   = findViewById(R.id.btnGuardarEspecialidad);


        idEspecialidad = getIntent().getIntExtra("EXTRA_ID_ESPECIALIDAD", -1);
        esEdicion = idEspecialidad != -1;
        if (esEdicion) {
            setTitle("Editar Especialidad");

            new Thread(() -> {
                Especialidad e = AppDatabase
                        .getInstance(this)
                        .dao_especialidad()
                        .buscarPorId(idEspecialidad);
                runOnUiThread(() -> {
                    if (e != null) {
                        etNombre.setText(e.getNombre());
                    }
                });
            }).start();
        } else {
            setTitle("Nueva Especialidad");
        }


        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();


            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                if (esEdicion) {

                    Especialidad e = new Especialidad(nombre);
                    e.setIdEspecialidad(idEspecialidad);
                    db.dao_especialidad().actualizarDatosEspecialidad(e);
                } else {

                    db.dao_especialidad().insertarEspecialidad(new Especialidad(nombre));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }
}
