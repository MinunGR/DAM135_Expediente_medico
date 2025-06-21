package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.RegistroMedico;

public class FormRegistroActivity extends AppCompatActivity {

    private static final int REQ_FOTO = 3001;

    private TextView tvFechaForm;
    private EditText etDiagnostico, etTratamiento, etNotas;
    private Button btnSelectFoto, btnGuardar;
    private ImageView imgPreview;

    private Uri fotoUri;
    private boolean esEdicion;
    private int idRegistro, idPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);

        tvFechaForm    = findViewById(R.id.tvFechaForm);
        etDiagnostico  = findViewById(R.id.etDiagnosticoForm);
        etTratamiento  = findViewById(R.id.etTratamientoForm);
        etNotas        = findViewById(R.id.etNotasForm);
        btnSelectFoto  = findViewById(R.id.btnSelectFotoForm);
        imgPreview     = findViewById(R.id.imgPreviewForm);
        btnGuardar     = findViewById(R.id.btnGuardarForm);

        // Recibir ID paciente y registro
        idPaciente  = getIntent().getIntExtra("EXTRA_ID_PACIENTE", -1);
        idRegistro  = getIntent().getIntExtra("EXTRA_ID_REGISTRO", -1);
        esEdicion   = idRegistro != -1;

        if (esEdicion) {
            setTitle("Editar Registro");
            // Cargar registro existente
            new Thread(() -> {
                RegistroMedico r = AppDatabase
                        .getInstance(this)
                        .registroMedicoDao()
                        .buscarPorId(idRegistro);
                runOnUiThread(() -> {
                    if (r != null) {
                        tvFechaForm.setText(
                                new java.text.SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm", java.util.Locale.getDefault()
                                ).format(new java.util.Date(r.getFechaRegistro()))
                        );
                        etDiagnostico.setText(r.getDiagnostico());
                        etTratamiento.setText(r.getTratamiento());
                        etNotas.setText(r.getNotas());
                        if (!r.getFotoUri().isEmpty()) {
                            fotoUri = Uri.parse(r.getFotoUri());
                            imgPreview.setImageURI(fotoUri);
                            imgPreview.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }).start();
        } else {
            setTitle("Nuevo Registro");
            tvFechaForm.setText(
                    new java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm", java.util.Locale.getDefault()
                    ).format(new java.util.Date())
            );
        }

        // Seleccionar foto
        btnSelectFoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(i, REQ_FOTO);
        });

        // Guardar registro
        btnGuardar.setOnClickListener(v -> {
            String diag = etDiagnostico.getText().toString().trim();
            String trat = etTratamiento.getText().toString().trim();
            String notas= etNotas.getText().toString().trim();
            if (diag.isEmpty() || trat.isEmpty()) {
                Toast.makeText(this, "Diagnóstico y tratamiento son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            long fecha = System.currentTimeMillis();
            String foto = fotoUri != null ? fotoUri.toString() : "";

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                if (esEdicion) {
                    RegistroMedico r = new RegistroMedico(
                            idPaciente, fecha, diag, trat, notas, foto
                    );
                    r.setIdRegistro(idRegistro);
                    db.registroMedicoDao().actualizarRegistro(r);
                } else {
                    db.registroMedicoDao()
                            .insertarRegistro(new RegistroMedico(
                                    idPaciente, fecha, diag, trat, notas, foto
                            ));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            getContentResolver().takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
            fotoUri = uri;
            imgPreview.setImageURI(uri);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }
}
