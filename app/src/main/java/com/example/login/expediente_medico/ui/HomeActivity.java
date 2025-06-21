package com.example.login.expediente_medico.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.R;

public class HomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Botón que abre la lista de doctores
        findViewById(R.id.btnIrDoctores).setOnClickListener(v -> {
            startActivity(new Intent(this, DoctoresActivity.class));
        });

        // Botón que abre la lista de pacientes
        findViewById(R.id.btnIrPacientes).setOnClickListener(v -> {
            startActivity(new Intent(this, PacientesActivity.class));
        });

        // Botón que abre la lista de especialidades
        findViewById(R.id.btnIrEspecialidades).setOnClickListener(v ->
                startActivity(new Intent(this, EspecialidadesActivity.class))
        );

        // Botón que abre la lista de citas
        findViewById(R.id.btnIrCitas).setOnClickListener(v ->
                startActivity(new Intent(this, CitasActivity.class))
        );

        // Botón que abre la lista de registro/historial
        findViewById(R.id.btnIrExpediente).setOnClickListener(v ->
                startActivity(new Intent(this, ExpedienteActivity.class))
        );

    }

}
