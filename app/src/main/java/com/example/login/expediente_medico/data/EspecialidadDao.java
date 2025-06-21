package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Especialidad.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface EspecialidadDao {
    // Inserta en db
    @Insert
    long insertarEspecialidad(Especialidad especialidad);
    // Obtiene todos
    @Query("SELECT * FROM especialidades")
    List<Especialidad> obtenerEspecialidades();
    // Actualiza en db
    @Update
    int actualizarDatosEspecialidad(Especialidad especialidad);
    // Elimina en db
    @Delete
    int eliminarEspecialidad(Especialidad especialidad);
    // Busca por id
    @Query("SELECT * FROM especialidades WHERE idEspecialidad = :idEspecialidad")
    Especialidad buscarPorId(int idEspecialidad);

}
