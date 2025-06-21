package com.example.login.expediente_medico.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;
import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Cita;
import com.example.login.expediente_medico.data.Doctor;
import com.example.login.expediente_medico.data.Paciente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FormCitaActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FECHA_HORA = 1001;
    private static final int REQUEST_CODE_SELECCIONAR_FOTO = 1002;

    private AutoCompleteTextView autoDoctor, autoPaciente;
    private Button btnSeleccionarFechaHora, btnGuardar;
    private TextView tvFechaHoraSeleccionada;
    private EditText etMotivo;

    private List<Doctor> listaDoctores;
    private List<Paciente> listaPacientes;
    private Doctor doctorSeleccionado;
    private Paciente pacienteSeleccionado;
    private long fechaHoraSeleccionada = -1;
    private Uri fotoUri;

    private boolean esEdicion;
    private int idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cita);


        autoDoctor = findViewById(R.id.autoDoctor);
        autoPaciente = findViewById(R.id.autoPaciente);
        btnSeleccionarFechaHora = findViewById(R.id.btnSeleccionarFechaHora);
        tvFechaHoraSeleccionada = findViewById(R.id.tvFechaHoraSeleccionada);
        etMotivo = findViewById(R.id.etMotivoCita);
        btnGuardar = findViewById(R.id.btnGuardarCita);


        idCita = getIntent().getIntExtra("EXTRA_ID_CITA", -1);
        esEdicion = idCita != -1;


        new Thread(() -> {
            listaDoctores = AppDatabase.getInstance(this).dao_doctor().obtenerDoctores();
            listaPacientes = AppDatabase.getInstance(this).dao_paciente().obtenerPacientes();
            runOnUiThread(() -> {
                ArrayAdapter<Doctor> docAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        listaDoctores
                );
                autoDoctor.setAdapter(docAdapter);
                autoDoctor.setOnItemClickListener((parent, view, position, id) -> {
                    doctorSeleccionado = (Doctor) parent.getItemAtPosition(position);
                });

                ArrayAdapter<Paciente> pacAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        listaPacientes
                );
                autoPaciente.setAdapter(pacAdapter);
                autoPaciente.setOnItemClickListener((parent, view, position, id) -> {
                    pacienteSeleccionado = (Paciente) parent.getItemAtPosition(position);
                });

                if (esEdicion) {

                    new Thread(() -> {
                        Cita cita = AppDatabase.getInstance(this)
                                .dao_cita().buscarCitaPorId(idCita);
                        runOnUiThread(() -> {
                            if (cita != null) {

                                for (int i = 0; i < listaDoctores.size(); i++) {
                                    if (listaDoctores.get(i).getIdDoctor() == cita.getDoctorId()) {
                                        autoDoctor.setText(listaDoctores.get(i).getNombre(), false);
                                        doctorSeleccionado = listaDoctores.get(i);
                                        break;
                                    }
                                }

                                for (int i = 0; i < listaPacientes.size(); i++) {
                                    if (listaPacientes.get(i).getIdPaciente() == cita.getPacienteId()) {
                                        autoPaciente.setText(listaPacientes.get(i).getNombre(), false);
                                        pacienteSeleccionado = listaPacientes.get(i);
                                        break;
                                    }
                                }
                                fechaHoraSeleccionada = cita.getFechaHora();
                                String fh = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(fechaHoraSeleccionada);
                                tvFechaHoraSeleccionada.setText(fh);
                                etMotivo.setText(cita.getMotivo());
                            }
                        });
                    }).start();
                }
            });
        }).start();


        btnSeleccionarFechaHora.setOnClickListener(v -> showDateTimePicker());


        btnGuardar.setOnClickListener(v -> {
            if (doctorSeleccionado == null || pacienteSeleccionado == null || fechaHoraSeleccionada < 0) {
                Toast.makeText(this, "Completa doctor, paciente y fecha/hora", Toast.LENGTH_SHORT).show();
                return;
            }
            String motivo = etMotivo.getText().toString().trim();
            if (motivo.isEmpty()) {
                Toast.makeText(this, "Escribe un motivo", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                if (esEdicion) {
                    Cita c = new Cita(
                            doctorSeleccionado.getIdDoctor(),
                            pacienteSeleccionado.getIdPaciente(),
                            fechaHoraSeleccionada,
                            motivo
                    );
                    c.setIdCita(idCita);
                    db.dao_cita().actualizarDatosCita(c);
                } else {
                    db.dao_cita().insertarCita(new Cita(
                            doctorSeleccionado.getIdDoctor(),
                            pacienteSeleccionado.getIdPaciente(),
                            fechaHoraSeleccionada,
                            motivo
                    ));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }

    private void showDateTimePicker() {
        final Calendar calendario = Calendar.getInstance();

        DatePickerDialog selectorFecha = new DatePickerDialog(
                this,
                (vistaFecha, anio, mes, dia) -> {
                    calendario.set(Calendar.YEAR, anio);
                    calendario.set(Calendar.MONTH, mes);
                    calendario.set(Calendar.DATE, dia);

                    TimePickerDialog selectorHora = new TimePickerDialog(
                            this,
                            (vistaHora, hora, minuto) -> {
                                calendario.set(Calendar.HOUR_OF_DAY, hora);
                                calendario.set(Calendar.MINUTE, minuto);

                                fechaHoraSeleccionada = calendario.getTimeInMillis();
                                String formatoFecha = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm", Locale.getDefault()
                                ).format(calendario.getTime());
                                tvFechaHoraSeleccionada.setText(formatoFecha);
                            },
                            calendario.get(Calendar.HOUR_OF_DAY),
                            calendario.get(Calendar.MINUTE),
                            true
                    );
                    selectorHora.show();
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        );

        selectorFecha.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        selectorFecha.show();
    }

}
