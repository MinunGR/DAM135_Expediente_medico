package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class DoctoresActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        // Inserta el fragment de doctores en el contenedor
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.cnt_doctor,
                        new DoctoresFragment()
                )
                .commit();
    }
}
