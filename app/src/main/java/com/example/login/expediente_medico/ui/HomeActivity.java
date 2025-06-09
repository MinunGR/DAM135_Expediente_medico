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
        findViewById(R.id.btnDoctores).setOnClickListener(v -> {
            startActivity(new Intent(this, DoctoresActivity.class));
        });

        // Botón que abre la lista de pacientes
        findViewById(R.id.btnPacientes).setOnClickListener(v -> {
            startActivity(new Intent(this, PacientesActivity.class));
        });


    }

}
