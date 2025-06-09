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
import androidx.room.Room;

import com.example.login.expediente_medico.data.AppDatabase;
import com.example.login.expediente_medico.ui.HomeActivity;

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
        // usa tu singleton que ya tiene fallbackToDestructiveMigrationFrom(...)
        AppDatabase bd = AppDatabase.obtenerInstancia(this);
        usuarioDao = bd.usuarioDao();

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
        String email    = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(email);
        nuevo.setContrasena(password);

        // CORRECCIÓN: movemos la inserción a un hilo de fondo
        new Thread(() -> {
            long id = usuarioDao.insertar(nuevo);

            runOnUiThread(() -> {
                if (id > 0) {
                    guardarSesion(email);
                    // Navegar al Home
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Error al registrar usuario",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }).start();
    }


    /*
        Inicia sesión comprobando credenciales contra la base de datos
     */
    private void iniciarSesion() {
        String email    = etCorreo.getText().toString().trim();
        String password = etContrasena.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ejecutamos la consulta en background
        new Thread(() -> {
            Usuario usuario = AppDatabase
                    .obtenerInstancia(this)
                    .usuarioDao()
                    .buscarPorCorreo(email);

            runOnUiThread(() -> {
                if (usuario != null && usuario.getContrasena().equals(password)) {
                    // 1) Guardar en SharedPreferences (opcional)
                    getSharedPreferences("sesion", MODE_PRIVATE)
                            .edit()
                            .putString("usuario_email", email)
                            .apply();

                    // 2) Navegar a HomeActivity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // cerramos la pantalla de login

                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }).start();
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