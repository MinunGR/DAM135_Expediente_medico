package com.example.login.expediente_medico.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;


import com.example.login.expediente_medico.data.Usuario;
import com.example.login.expediente_medico.data.UsuarioDao;

@Database(
        entities = { Usuario.class, Doctor.class,
                Paciente.class , Especialidad.class, Cita.class,
                RegistroMedico.class},
        version = 6,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    private static final String NOMBRE_BD = "expediente_db";
    private static AppDatabase INSTANCIA;
    public abstract DoctorDao dao_doctor();
    public abstract RegistroMedicoDao registroMedicoDao();
    public abstract EspecialidadDao dao_especialidad();
    public abstract UsuarioDao dao_usuario();
    public abstract PacienteDao dao_paciente();
    public abstract CitaDao dao_cita();



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
