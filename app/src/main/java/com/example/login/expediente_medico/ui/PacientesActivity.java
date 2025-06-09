package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class PacientesActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        // Inserta el fragment de pacientes en el contenedor
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container_pacientes,
                        new PacientesFragment()
                )
                .commit();
    }
}
