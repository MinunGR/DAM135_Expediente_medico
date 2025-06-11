package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class CitasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        // Inserta el fragmento de citas
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container_citas,
                        new CitasFragment()
                )
                .commit();
    }
}
