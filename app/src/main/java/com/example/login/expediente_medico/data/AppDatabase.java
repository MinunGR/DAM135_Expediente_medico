package com.example.login.expediente_medico.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


import com.example.login.expediente_medico.data.Doctor;
import com.example.login.expediente_medico.data.DoctorDao;
import com.example.login.expediente_medico.data.Paciente;
import com.example.login.expediente_medico.data.PacienteDao;
import com.example.login.expediente_medico.Usuario;
import com.example.login.expediente_medico.UsuarioDao;
import com.example.login.expediente_medico.data.Especialidad;
import com.example.login.expediente_medico.data.EspecialidadDao;
import com.example.login.expediente_medico.data.Consultorio;
import com.example.login.expediente_medico.data.ConsultorioDao;


// clase de base de datos principal de Room
@Database(
        entities = { Usuario.class, Doctor.class, Paciente.class , Especialidad.class, Consultorio.class},
        version = 4,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    private static final String NOMBRE_BD = "citas_medicas_db";
    private static AppDatabase INSTANCIA;

    /*
        Obtiene el DAO de Usuario
     */
    public abstract UsuarioDao usuarioDao();

    /*
        Obtiene el DAO de Doctor
     */
    public abstract DoctorDao doctorDao();

    /*
        Obtiene el DAO de paciente
     */
    public abstract PacienteDao pacienteDao();

    /*
        Obtiene el DAO de especialidad
     */
    public abstract EspecialidadDao especialidadDao();


    /*
        Obtiene el DAO de consultorios.
     */
    public abstract ConsultorioDao consultorioDao();

    public static synchronized AppDatabase obtenerInstancia(Context contexto) {
        if (INSTANCIA == null) {
            INSTANCIA = Room.databaseBuilder(
                            contexto.getApplicationContext(),
                            AppDatabase.class,
                            NOMBRE_BD
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCIA;
    }
}
