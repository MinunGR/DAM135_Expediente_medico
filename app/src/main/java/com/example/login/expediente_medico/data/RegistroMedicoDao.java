package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Registro.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface RegistroMedicoDao {
    // Inserta en db
    @Insert
    long insertarRegistro(RegistroMedico registro);
    // Actualiza en db
    @Update
    int actualizarRegistro(RegistroMedico registro);
    // Elimina en db
    @Delete
    int eliminarRegistro(RegistroMedico registro);
    // Obtiene registros por paciente
    @Query("SELECT * FROM registros_medicos WHERE pacienteId = :pacienteId ORDER BY fechaRegistro DESC")
    List<RegistroMedico> obtenerRegistrosPorPaciente(int pacienteId);
    // Obtiene por id
    @Query("SELECT * FROM registros_medicos WHERE idRegistro = :idRegistro")
    RegistroMedico buscarPorId(int idRegistro);
}
