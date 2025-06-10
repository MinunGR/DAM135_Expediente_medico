package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class ConsultoriosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultorios);
        // Inserta el fragment de consultorios  en el contenedor
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container_consultorios,
                        new ConsultoriosFragment()
                )
                .commit();
    }
}
