package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class DoctoresActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctores);

        // Inyecta tu fragmento de lista de doctores
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container_doctores,
                        new DoctoresFragment()
                )
                .commit();
    }
}
