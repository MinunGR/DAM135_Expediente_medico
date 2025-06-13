package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Para operaciones CRUD sobre la entidad
@Dao
public interface RegistroMedicoDao {

    // Insertar
    @Insert
    long insertarRegistro(RegistroMedico registro);

    // Actualizar
    @Update
    int actualizarRegistro(RegistroMedico registro);

    // Eliminar
    @Delete
    int eliminarRegistro(RegistroMedico registro);

    // Obtener todos
    @Query("SELECT * FROM registros_medicos WHERE pacienteId = :pacienteId ORDER BY fechaRegistro DESC")
    List<RegistroMedico> obtenerRegistrosPorPaciente(int pacienteId);

    // Buscar por id
    @Query("SELECT * FROM registros_medicos WHERE idRegistro = :idRegistro")
    RegistroMedico buscarRegistroPorId(int idRegistro);
}
