package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class HomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Botón que abre la lista de doctores
        findViewById(R.id.btnDoctores).setOnClickListener(v -> {
            startActivity(new Intent(this, DoctoresActivity.class));
        });

        // Botón que abre la lista de pacientes
        findViewById(R.id.btnPacientes).setOnClickListener(v -> {
            startActivity(new Intent(this, PacientesActivity.class));
        });

        // Botón que abre la lista de especialidades
        findViewById(R.id.btnEspecialidades).setOnClickListener(v ->
                startActivity(new Intent(this, EspecialidadesActivity.class))
        );

        // Botón que abre la lista de consultorios
        findViewById(R.id.btnConsultorios).setOnClickListener(v ->
                startActivity(new Intent(this, ConsultoriosActivity.class))
        );

        // Botón que abre la lista de citas
        findViewById(R.id.btnCitas).setOnClickListener(v ->
                startActivity(new Intent(this, CitasActivity.class))
        );

        // Botón que abre la lista de registro/historial
        findViewById(R.id.btnExpediente).setOnClickListener(v ->
                startActivity(new Intent(this, ExpedienteActivity.class))
        );

    }

}
