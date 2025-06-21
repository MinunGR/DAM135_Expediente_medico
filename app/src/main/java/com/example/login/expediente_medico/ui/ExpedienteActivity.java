package com.example.login.expediente_medico.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login.expediente_medico.R;

public class ExpedienteActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expediente);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.cnt_expediente,
                        new ExpedienteFragment()
                )
                .commit();
    }
}
