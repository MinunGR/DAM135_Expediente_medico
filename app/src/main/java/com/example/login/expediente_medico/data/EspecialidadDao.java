package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface EspecialidadDao {

    // Insertar una especialidad
    @Insert
    long insertarEspecialidad(Especialidad especialidad);

    // Obtener todos
    @Query("SELECT * FROM especialidades")
    List<Especialidad> obtenerEspecialidades();

    // Actualizar los campos
    @Update
    int actualizarEspecialidad(Especialidad especialidad);

    // Eliminar una especialidad
    @Delete
    int eliminarEspecialidad(Especialidad especialidad);

    // Buscar una especialidad por id
    @Query("SELECT * FROM especialidades WHERE idEspecialidad = :idEspecialidad")
    Especialidad buscarEspecialidadPorId(int idEspecialidad);

}
