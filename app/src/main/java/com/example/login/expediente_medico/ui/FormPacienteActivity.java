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
import com.example.login.expediente_medico.data.Paciente;

public class FormPacienteActivity extends AppCompatActivity{
    private static final int REQUEST_CODE_SELECCIONAR_FOTO = 2001;

    private ImageView imgPreview;
    private Button btnSeleccionarFoto, btnGuardar;
    private EditText etNombre, etContacto;

    // Guardamos la URI elegida
    private Uri fotoUriSeleccionada = null;
    private boolean esEdicion;
    private int idPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_paciente);

        // enlazar vistas
        imgPreview            = findViewById(R.id.imgPreviewPaciente);
        btnSeleccionarFoto    = findViewById(R.id.btnSeleccionarFotoPaciente);
        etNombre              = findViewById(R.id.etNombrePaciente);
        etContacto            = findViewById(R.id.etContactoPaciente);
        btnGuardar            = findViewById(R.id.btnGuardarPaciente);

        // Detectamos si es creación o edición
        idPaciente = getIntent().getIntExtra("EXTRA_ID_PACIENTE", -1);

        esEdicion = idPaciente != -1;
        if (esEdicion) {
            setTitle("Editar Paciente");
            // Cargar datos
            new Thread(() -> {
                Paciente p = AppDatabase
                        .getInstance(this)
                        .dao_paciente()
                        .buscarPacientePorId(idPaciente);
                runOnUiThread(() -> {
                    if (p != null) {
                        etNombre.setText(p.getNombre());
                        etContacto.setText(p.getDatosContacto());
                        if (!p.getFotoUri().isEmpty()) {
                            fotoUriSeleccionada = Uri.parse(p.getFotoUri());
                            imgPreview.setImageURI(fotoUriSeleccionada);
                        }
                    }
                });
            }).start();
        } else {
            setTitle("Nuevo Paciente");
        }

        // Al hacer clic, abrimos la galería para elegir imagen
        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            );
            startActivityForResult(intent, REQUEST_CODE_SELECCIONAR_FOTO);
        });

        // Guardar (insertar o actualizar)
        btnGuardar.setOnClickListener(v -> {
            String nombre   = etNombre.getText().toString().trim();
            String contacto = etContacto.getText().toString().trim();
            if (nombre.isEmpty() || contacto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertimos la URI a string (o cadena vacía si no seleccionó foto)
            String fotoStr = fotoUriSeleccionada != null
                    ? fotoUriSeleccionada.toString() : "";

            // Guardar en BD en hilo de fondo
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                if (esEdicion) {
                    Paciente p = new Paciente(nombre, contacto, fotoStr);
                    p.setIdPaciente(idPaciente);
                    db.dao_paciente().actualizarPaciente(p);
                } else {
                    db.dao_paciente().insertarPaciente(new Paciente(nombre, contacto, fotoStr));
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
