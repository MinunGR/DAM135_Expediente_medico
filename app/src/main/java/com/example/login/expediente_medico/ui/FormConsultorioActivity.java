package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Consultorio;

public class FormConsultorioActivity extends AppCompatActivity {

    private EditText etNombre, etUbicacion;
    private Button btnGuardar;
    private boolean esEdicion;
    private int idConsultorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_consultorio);

        etNombre    = findViewById(R.id.etNombreConsultorio);
        etUbicacion = findViewById(R.id.etUbicacionConsultorio);
        btnGuardar  = findViewById(R.id.btnGuardarConsultorio);

        // Detectar si venimos a editar
        idConsultorio = getIntent().getIntExtra("EXTRA_ID_CONSULTORIO", -1);
        esEdicion = idConsultorio != -1;
        if (esEdicion) {
            setTitle("Editar Consultorio");
            new Thread(() -> {
                Consultorio c = AppDatabase
                        .getInstance(this)
                        .dao_consultorio()
                        .buscarPorId(idConsultorio);
                runOnUiThread(() -> {
                    if (c != null) {
                        etNombre.setText(c.getNombre());
                        etUbicacion.setText(c.getUbicacion());
                    }
                });
            }).start();
        } else {
            setTitle("Nuevo Consultorio");
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre  = etNombre.getText().toString().trim();
            String ubicacion = etUbicacion.getText().toString().trim();
            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                if (esEdicion) {
                    Consultorio c = new Consultorio(nombre, ubicacion);
                    c.setIdConsultorio(idConsultorio);
                    db.dao_consultorio().actualizarDatosConsultorio(c);
                } else {
                    db.dao_consultorio().insertarConsultorio(new Consultorio(nombre, ubicacion));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }
}
