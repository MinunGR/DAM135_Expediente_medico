package com.example.login.expediente_medico.ui;

import static com.example.login.expediente_medico.data.AppDatabase.getInstance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Doctor;
import com.example.login.expediente_medico.data.Especialidad;

import java.util.ArrayList;
import java.util.List;

public class FormDoctorActivity extends AppCompatActivity {
    private static final int REQ_PHOTO = 2001;

    private EditText etNombre, etHorarios;
    private AutoCompleteTextView autoEsp;
    private ImageView imgPreview;
    private Button btnPickPhoto, btnGuardar;

    private Uri fotoUri;
    private boolean esEdicion;
    private int idDoctor;
    private List<Especialidad> listaEsp;
    private List<String> nombresEsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_doctor);

        etNombre   = findViewById(R.id.etNombreDoctor);
        autoEsp    = findViewById(R.id.autoEspecialidad);
        etHorarios = findViewById(R.id.etHorariosDoctor);
        btnPickPhoto = findViewById(R.id.btnSeleccionarFotoDoctor);
        imgPreview  = findViewById(R.id.imgPreviewDoctor);
        btnGuardar  = findViewById(R.id.btnGuardarDoctor);

        idDoctor   = getIntent().getIntExtra("EXTRA_ID_DOCTOR", -1);
        esEdicion  = idDoctor != -1;
        setTitle(esEdicion ? "Editar Doctor" : "Nuevo Doctor");


        new Thread(() -> {
            listaEsp = AppDatabase
                    .getInstance(this)
                    .dao_especialidad()
                    .obtenerEspecialidades();

            nombresEsp = new ArrayList<>();
            for (Especialidad e : listaEsp) {
                nombresEsp.add(e.getNombre());
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        nombresEsp
                );
                autoEsp.setAdapter(adapter);

                if (esEdicion) {

                    new Thread(() -> {
                        Doctor d = getInstance(this)
                                .dao_doctor()
                                .buscarPorId(idDoctor);
                        runOnUiThread(() -> {
                            if (d != null) {
                                etNombre.setText(d.getNombre());
                                etHorarios.setText(d.getHorariosDisponibles());
                                autoEsp.setText(d.getEspecialidad(), false);
                                if (!d.getFotoUri().isEmpty()) {
                                    fotoUri = Uri.parse(d.getFotoUri());
                                    imgPreview.setImageURI(fotoUri);
                                    imgPreview.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }).start();
                }
            });
        }).start();


        btnPickPhoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(i, REQ_PHOTO);
        });


        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String esp     = autoEsp.getText().toString().trim();
            String horas   = etHorarios.getText().toString().trim();

            if (nombre.isEmpty() || esp.isEmpty() || horas.isEmpty()) {
                Toast.makeText(this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = getInstance(this);
                if (esEdicion) {
                    Doctor d = new Doctor(nombre, esp,
                            fotoUri==null?"":fotoUri.toString(),
                            horas);
                    d.setIdDoctor(idDoctor);
                    db.dao_doctor().actualizarDatosDoctor(d);
                } else {
                    db.dao_doctor().insertarDoctor(
                            new Doctor(nombre, esp,
                                    fotoUri==null?"":fotoUri.toString(),
                                    horas)
                    );
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == REQ_PHOTO && res == RESULT_OK && data!=null && data.getData()!=null) {
            fotoUri = data.getData();
            getContentResolver().takePersistableUriPermission(
                    fotoUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imgPreview.setImageURI(fotoUri);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }
}
