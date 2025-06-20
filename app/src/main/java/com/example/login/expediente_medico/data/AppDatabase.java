package com.example.login.expediente_medico.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


import com.example.login.expediente_medico.Usuario;
import com.example.login.expediente_medico.UsuarioDao;


// clase de base de datos principal de Room
@Database(
        entities = { Usuario.class, Doctor.class,
                Paciente.class , Especialidad.class,
                Consultorio.class, Cita.class,
                RegistroMedico.class},
        version = 6,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    private static final String NOMBRE_BD = "expediente_db";
    private static AppDatabase INSTANCIA;

    /*
        Obtiene el DAO de Usuario
     */
    public abstract UsuarioDao dao_usuario();

    /*
        Obtiene el DAO de Doctor
     */
    public abstract DoctorDao dao_doctor();

    /*
        Obtiene el DAO de paciente
     */
    public abstract PacienteDao dao_paciente();

    /*
        Obtiene el DAO de especialidad
     */
    public abstract EspecialidadDao dao_especialidad();


    /*
        Obtiene el DAO de consultorios.
     */
    public abstract ConsultorioDao dao_consultorio();

    /*
        Obtiene el DAO de citas
     */
    public abstract CitaDao dao_cita();

    /*
        Obtiene el DAO de registro medico
     */
    public abstract RegistroMedicoDao registroMedicoDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCIA == null) {
            INSTANCIA = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            NOMBRE_BD
                    )
                    .fallbackToDestructiveMigrationFrom(1, 2, 3)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
        }
        return INSTANCIA;
    }
}
