package com.example.login.expediente_medico.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class CitaFormActivity extends AppCompatActivity {

    private Spinner spinnerDoctor, spinnerPaciente;
    private Button btnSeleccionarFechaHora, btnGuardar;
    private TextView tvFechaHora;
    private EditText etMotivo;

    private List<Doctor> listaDoctores;
    private List<Paciente> listaPacientes;
    private long fechaHoraSeleccionada = -1;

    private boolean esEdicion;
    private int idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cita);

        spinnerDoctor           = findViewById(R.id.spinnerDoctor);
        spinnerPaciente         = findViewById(R.id.spinnerPaciente);
        btnSeleccionarFechaHora = findViewById(R.id.btnSeleccionarFechaHora);
        tvFechaHora             = findViewById(R.id.tvFechaHoraSeleccionada);
        etMotivo                = findViewById(R.id.etMotivoCita);
        btnGuardar              = findViewById(R.id.btnGuardarCita);

        // Cargar doctores y pacientes para los spinners
        new Thread(() -> {
            listaDoctores  = AppDatabase.obtenerInstancia(this).doctorDao().obtenerDoctores();
            listaPacientes = AppDatabase.obtenerInstancia(this).pacienteDao().obtenerPacientes();
            runOnUiThread(() -> {
                spinnerDoctor.setAdapter(new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, listaDoctores
                ));
                spinnerPaciente.setAdapter(new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_item, listaPacientes
                ));
            });
        }).start();

        // Detectar si venimos a editar
        idCita = getIntent().getIntExtra("EXTRA_ID_CITA", -1);
        esEdicion = idCita != -1;
        if (esEdicion) {
            setTitle("Editar Cita");
            new Thread(() -> {
                Cita c = AppDatabase.obtenerInstancia(this).citaDao().buscarCitaPorId(idCita);
                runOnUiThread(() -> {
                    if (c != null) {
                        // Seleccionar doctor y paciente en spinner
                        for (int i = 0; i < listaDoctores.size(); i++) {
                            if (listaDoctores.get(i).getIdDoctor() == c.getDoctorId()) {
                                spinnerDoctor.setSelection(i);
                                break;
                            }
                        }
                        for (int i = 0; i < listaPacientes.size(); i++) {
                            if (listaPacientes.get(i).getIdPaciente() == c.getPacienteId()) {
                                spinnerPaciente.setSelection(i);
                                break;
                            }
                        }
                        fechaHoraSeleccionada = c.getFechaHora();
                        String fh = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                .format(fechaHoraSeleccionada);
                        tvFechaHora.setText(fh);
                        etMotivo.setText(c.getMotivo());
                    }
                });
            }).start();
        } else {
            setTitle("Nueva Cita");
        }

        // Selector fecha y hora
        btnSeleccionarFechaHora.setOnClickListener(v -> showDateTimePicker());

        // Guardar
        btnGuardar.setOnClickListener(v -> {
            int posDoc = spinnerDoctor.getSelectedItemPosition();
            int posPac = spinnerPaciente.getSelectedItemPosition();
            String motivo = etMotivo.getText().toString().trim();

            if (posDoc < 0 || posPac < 0 || fechaHoraSeleccionada < 0 || motivo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            int doctorId  = listaDoctores.get(posDoc).getIdDoctor();
            int pacienteId= listaPacientes.get(posPac).getIdPaciente();

            new Thread(() -> {
                AppDatabase db = AppDatabase.obtenerInstancia(this);
                if (esEdicion) {
                    Cita c = new Cita(doctorId, pacienteId, fechaHoraSeleccionada, motivo);
                    c.setIdCita(idCita);
                    db.citaDao().actualizarCita(c);
                } else {
                    db.citaDao().insertarCita(new Cita(doctorId, pacienteId, fechaHoraSeleccionada, motivo));
                }
                runOnUiThread(this::finish);
            }).start();
        });
    }

    // para fecha
    private void showDateTimePicker() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, day);
                    new TimePickerDialog(this,
                            (tView, hour, minute) -> {
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minute);
                                fechaHoraSeleccionada = c.getTimeInMillis();
                                String fh = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(c.getTime());
                                tvFechaHora.setText(fh);
                            },
                            c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true
                    ).show();
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
