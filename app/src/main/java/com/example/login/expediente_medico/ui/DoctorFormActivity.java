package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Doctor;

public class DoctorFormActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECCIONAR_FOTO = 1001;

    private ImageView imgPreview;
    private Button btnSeleccionarFoto;
    private EditText etNombre, etEspecialidad, etHorarios;
    private Button btnGuardar;

    // Guardamos la URI elegida
    private Uri fotoUriSeleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_doctor);

        // enlazar views
        imgPreview         = findViewById(R.id.imgPreviewDoctor);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        etNombre           = findViewById(R.id.etNombreDoctor);
        etEspecialidad     = findViewById(R.id.etEspecialidadDoctor);
        etHorarios         = findViewById(R.id.etHorariosDoctor);
        btnGuardar         = findViewById(R.id.btnGuardarDoctor);


        // Detectamos si es creación o edición
        int idDoctor = getIntent().getIntExtra("EXTRA_ID_DOCTOR", -1);
        boolean esEdicion = idDoctor != -1;

        if (esEdicion) {
            setTitle("Editar Doctor");
            // Carga datos en UI en fondo
            new Thread(() -> {
                Doctor doc = AppDatabase.obtenerInstancia(this)
                        .doctorDao()
                        .buscarDoctorPorId(idDoctor);
                runOnUiThread(() -> {
                    if (doc != null) {
                        etNombre.setText(doc.getNombre());
                        etEspecialidad.setText(doc.getEspecialidad());
                        etHorarios.setText(doc.getHorariosDisponibles());
                        if (!doc.getFotoUri().isEmpty()) {
                            fotoUriSeleccionada = Uri.parse(doc.getFotoUri());
                            imgPreview.setImageURI(fotoUriSeleccionada);
                        }
                    }
                });
            }).start();
        } else {
            setTitle("Nuevo Doctor");
        }

        // Al hacer clic, abrimos la galería para elegir imagen
        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            // Pedimos permiso para leer y persistir la URI
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            );
            startActivityForResult(intent, REQUEST_CODE_SELECCIONAR_FOTO);
        });

        // Guardar (insertar o actualizar)
        btnGuardar.setOnClickListener(v -> {
            String nombre       = etNombre.getText().toString().trim();
            String especialidad = etEspecialidad.getText().toString().trim();
            String horarios     = etHorarios.getText().toString().trim();

            // Validar campos completados
            if (nombre.isEmpty() || especialidad.isEmpty() || horarios.isEmpty()) {
                Toast.makeText(
                        DoctorFormActivity.this,
                        "Por favor, completa todos los campos",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Convertimos la URI a string (o cadena vacía si no seleccionó foto)
            String fotoUriStr = fotoUriSeleccionada != null
                    ? fotoUriSeleccionada.toString()
                    : "";

            // Guardar en BD en hilo de fondo
            new Thread(() -> {
                AppDatabase db = AppDatabase.obtenerInstancia(this);
                if (esEdicion) {
                    // Actualizar un doctor existente
                    Doctor doc = new Doctor(
                            etNombre.getText().toString().trim(),
                            etEspecialidad.getText().toString().trim(),
                            fotoUriStr,
                            etHorarios.getText().toString().trim()
                    );
                    doc.setIdDoctor(idDoctor);
                    db.doctorDao().actualizarDoctor(doc);
                } else {
                    // Insertar nuevo
                    db.doctorDao().insertarDoctor(new Doctor(
                            etNombre.getText().toString().trim(),
                            etEspecialidad.getText().toString().trim(),
                            fotoUriStr,
                            etHorarios.getText().toString().trim()
                    ));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECCIONAR_FOTO
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri uri = data.getData();

            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            fotoUriSeleccionada = uri;
            imgPreview.setImageURI(uri);
        }
    }

}
