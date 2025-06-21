package com.example.login.expediente_medico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.data.Usuario;
import com.example.login.expediente_medico.data.UsuarioDao;
import com.example.login.expediente_medico.ui.HomeActivity;

public class MainActivity extends AppCompatActivity {

    // Elementos de la UI
    private EditText inpCorreo, inpContra, inpRepContra;
    private Button btnLoginReg;
    private TextView btnIrRegistro, lblTituloInicio, lblSubtituloInicio;

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "citas_prefs";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";

    // Boolean para indicar modo de registro
    private boolean form_mode = false;

    // Campos formulario
    private String email, password, cnfPassword;


    // Room Database
    private AppDatabase db_app;
    // Instancia Dao
    private UsuarioDao dao_usuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPreferences y AppDatabase
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        AppDatabase bd = AppDatabase.getInstance(this);
        dao_usuario = bd.dao_usuario();

        // Vinculamos elementos
        lblTituloInicio = findViewById(R.id.lblTituloInicio);
        lblSubtituloInicio = findViewById(R.id.lblSubtituloInicio);
        inpCorreo = findViewById(R.id.inpCorreo);
        inpContra = findViewById(R.id.inpContra);
        inpRepContra = findViewById(R.id.inpRepContra);
        btnLoginReg = findViewById(R.id.btnLoginReg);
        btnIrRegistro = findViewById(R.id.btnRegistro);

        this.buttonInit();

        // Inicializar estado de vistas
        actualizarModo();
    }

    // Añadimos los litener a los botones
    public void buttonInit(){
        // Listener para cambiar entre formularios
        btnIrRegistro.setOnClickListener(v -> {
            form_mode = !form_mode;
            actualizarModo();
        });

        // Listener botón de registros
        btnLoginReg.setOnClickListener(v -> {
            if (formValidator()) {
                if (form_mode) registrarUsuario();
                else login();
            }
        });


    }

    // Función para cambiar entre modos de formulario
    private void actualizarModo() {
        if (form_mode) {
            lblTituloInicio.setText(R.string.register_title);
            lblSubtituloInicio.setText(R.string.campos_vacios);
            btnLoginReg.setText(R.string.guardar);
            btnIrRegistro.setText(R.string.login_form_hint);
            inpRepContra.setVisibility(View.VISIBLE);
        } else {
            lblTituloInicio.setText(R.string.login_title);
            lblSubtituloInicio.setText(R.string.login_subtitle);
            btnLoginReg.setText(R.string.btn_login);
            btnIrRegistro.setText(R.string.register_form_hint);
            inpRepContra.setVisibility(View.GONE);
        }
    }


    private boolean formValidator() {
        email = inpCorreo.getText().toString().trim();
        password = inpContra.getText().toString().trim();

        // Validación de correo, si el campo está vacio o el formato del correo está invalido, notificamos
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inpCorreo.setError("Formato de correo inválido");
            inpCorreo.requestFocus();
            return false;
        }

        // Validación de contraseña que tenga que tener minimo 6 digitos
        if (password.length() < 5 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            inpContra.setError("Contraseña debe ser alfanumérica y tener al menos 5 caracteres");
            inpContra.requestFocus();
            return false;
        }

        // Si es registro, se valida la coincidencia de contraseñas
        if (form_mode) {
            cnfPassword = inpRepContra.getText().toString().trim();
            if (!password.equals(cnfPassword)) {
                inpRepContra.setError("Las contraseñas no coinciden");
                inpRepContra.requestFocus();
                return false;
            }
        }

        return true;
    }


    private void registrarUsuario() {
        email    = inpCorreo.getText().toString().trim();
        password = inpContra.getText().toString().trim();

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(email);
        nuevo.setContrasena(password);
        new Thread(() -> {
            long id = dao_usuario.insertar(nuevo);

            runOnUiThread(() -> {
                if (id > 0) {
                    saveSession(email);
                    // Navegar al Home
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Ocurrió un error al registrar usuario",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }).start();
    }


    private void login() {
        email = inpCorreo.getText().toString().trim();
        password = inpContra.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Usuario user = AppDatabase
                    .getInstance(this)
                    .dao_usuario()
                    .buscarPorCorreo(email);

            runOnUiThread(() -> {
                if (user != null && user.getContrasena().equals(password)) {
                    getSharedPreferences("sesion", MODE_PRIVATE)
                            .edit()
                            .putString("usuario_email", email)
                            .apply();


                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Correo o contraseña invalidos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }).start();
    }


    /*
        Guardar datos en SharedPreferences
     */
    private void saveSession(String email) {
        prefs.edit()
                .putString(KEY_USER_EMAIL, email)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }


    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}