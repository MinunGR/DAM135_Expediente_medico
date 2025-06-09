package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


// Para operaciones CRUD sobre la entidad
@Dao
public interface PacienteDao {

    // Insertar un paciente
    @Insert
    long insertarPaciente(Paciente paciente);

    // Obtener todos
    @Query("SELECT * FROM pacientes")
    List<Paciente> obtenerPacientes();

    // Actualizar los campos
    @Update
    int actualizarPaciente(Paciente paciente);

    // Eliminar un ductor
    @Delete
    int eliminarPaciente(Paciente paciente);

    // Busca un doctor por el id
    @Query("SELECT * FROM pacientes WHERE idPaciente = :idPaciente")
    Paciente buscarPacientePorId(int idPaciente);
}
