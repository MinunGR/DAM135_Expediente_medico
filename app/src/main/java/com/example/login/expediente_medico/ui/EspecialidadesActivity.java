package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class EspecialidadesActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidades);

        // Incrusta el fragmento de especialidades
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container_especialidades,
                        new EspecialidadesFragment()
                )
                .commit();
    }
}
