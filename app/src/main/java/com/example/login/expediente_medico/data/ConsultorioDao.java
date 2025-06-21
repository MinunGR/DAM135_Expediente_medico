package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Consultorio.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface ConsultorioDao {
    // Inserta en db
    @Insert
    long insertarConsultorio(Consultorio consultorio);
    // Obtiene todos
    @Query("SELECT * FROM consultorios")
    List<Consultorio> obtenerTodosLosConsultorios();
    // Actualiza en db
    @Update
    int actualizarDatosConsultorio(Consultorio consultorio);
    // Elimina en db
    @Delete
    int eliminarConsultorio(Consultorio consultorio);
    // Obtiene por id
    @Query("SELECT * FROM consultorios WHERE idConsultorio = :idConsultorio")
    Consultorio buscarPorId(int idConsultorio);

}
