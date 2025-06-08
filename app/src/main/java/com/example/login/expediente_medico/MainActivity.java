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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo, etContrasena, etRepetirContrasena;
    private Button btnIniciarRegistrar;
    private TextView tvCambiarModo, tvTituloLogin;

    // SharedPreferences
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "citas_prefs";
    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    // Para altenar entre el login y el registro
    private boolean modoRegistro = false;

    // Room
    private AppDatabase db;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Inicializar Room (en producción evita allowMainThreadQueries)
        db = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "citas_medicas.db")
                .allowMainThreadQueries()  // solo para desarrollo
                .build();
        usuarioDao = db.usuarioDao();

        // Vincular vistas
        tvTituloLogin = findViewById(R.id.tvTituloLogin);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        etRepetirContrasena = findViewById(R.id.etRepetirContrasena);
        btnIniciarRegistrar = findViewById(R.id.btnIniciarRegistrar);
        tvCambiarModo = findViewById(R.id.tvCambiarModo);

        // Configurar listener para cambiar entre login y registro
        tvCambiarModo.setOnClickListener(v -> {
            modoRegistro = !modoRegistro;
            actualizarModo();
        });

        // Listener del botón principal
        btnIniciarRegistrar.setOnClickListener(v -> {
            if (validarCampos()) {
                if (modoRegistro) {
                    registrarUsuario();
                } else {
                    iniciarSesion();
                }
            }
        });

        // Inicializar estado de vistas
        actualizarModo();
    }

    /*
        Altenar la vista entre entre el modo Login y Registro
     */
    private void actualizarModo() {
        if (modoRegistro) {
            tvTituloLogin.setText("Registro");
            btnIniciarRegistrar.setText("Registrarse");
            tvCambiarModo.setText("¿Ya tienes cuenta? Inicia sesión");
            etRepetirContrasena.setVisibility(View.VISIBLE);
        } else {
            tvTituloLogin.setText("Iniciar Sesión");
            btnIniciarRegistrar.setText("Iniciar Sesión");
            tvCambiarModo.setText("¿No tienes cuenta? Regístrate");
            etRepetirContrasena.setVisibility(View.GONE);
        }
    }

    /*
        Validaciones
     */
    private boolean validarCampos() {
        String email = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();

        // Validación de correo, si el campo está vacio o el formato del correo está invalido, notificamos
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etCorreo.setError("Formato de correo inválido");
            etCorreo.requestFocus();
            return false;
        }

        // Validación de contraseña que tenga que tener minimo 6 digitos
        if (password.length() < 6) {
            etContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            etContrasena.requestFocus();
            return false;
        }

        // Si es registro, se valida la coincidencia de contraseñas
        if (modoRegistro) {
            String repeat = etRepetirContrasena.getText().toString().trim();
            if (!password.equals(repeat)) {
                etRepetirContrasena.setError("Las contraseñas no coinciden");
                etRepetirContrasena.requestFocus();
                return false;
            }
        }

        return true;
    }

    /*
        Registrar un nuevo usuario en la base de datos
     */
    private void registrarUsuario() {
        String email = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();

        // TODO: Podrías hashear la contraseña antes de guardar
        Usuario nuevo = new Usuario();
        nuevo.setCorreo(email);
        nuevo.setContrasena(password);

        long id = usuarioDao.insertar(nuevo);
        if (id > 0) {
            guardarSesion(email);
            irActividadPrincipal();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Inicia sesión comprobando credenciales contra la base de datos
     */
    private void iniciarSesion() {
        String email = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();

        Usuario existente = usuarioDao.buscarPorCorreo(email);
        if (existente != null && existente.getContrasena().equals(password)) {
            guardarSesion(email);
            irActividadPrincipal();
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Guardar datos de sesión en SharedPreferences
     */
    private void guardarSesion(String email) {
        prefs.edit()
                .putString(KEY_USER_EMAIL, email)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    /*
        Abre la Activity principal
     */
    private void irActividadPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // se finaliza
    }
}