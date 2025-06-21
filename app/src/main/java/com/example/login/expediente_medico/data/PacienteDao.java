package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Paciente.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface PacienteDao {
    // Inserta en db
    @Insert
    long insertarPaciente(Paciente paciente);
    // Obtiene todos
    @Query("SELECT * FROM pacientes")
    List<Paciente> obtenerPacientes();
    // Actualiza en db
    @Update
    int actualizarDatosPaciente(Paciente paciente);
    // Elimina en db
    @Delete
    int eliminarPaciente(Paciente paciente);
    // Obtiene por id
    @Query("SELECT * FROM pacientes WHERE idPaciente = :idPaciente")
    Paciente buscarPorId(int idPaciente);
}
