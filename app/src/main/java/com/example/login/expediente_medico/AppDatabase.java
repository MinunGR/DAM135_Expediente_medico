package com.example.login.expediente_medico;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// clase de base de datos principal de Room
@Database(entities = {Usuario.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /*
        Obtiene el DAO de Usuario.
     */
    public abstract UsuarioDao usuarioDao();
}
