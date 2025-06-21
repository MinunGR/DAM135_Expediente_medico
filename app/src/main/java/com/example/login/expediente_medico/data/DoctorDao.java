package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Doctor.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface DoctorDao {
    // Inserta en db
    @Insert
    long insertarDoctor(Doctor doctor);
    // Obtiene todos
    @Query("SELECT * FROM doctores")
    List<Doctor> obtenerDoctores();
    // Actualiza en db
    @Update
    int actualizarDatosDoctor(Doctor doctor);
    // Elimina en db
    @Delete
    int eliminarDoctor(Doctor doctor);
    // Busca por id
    @Query("SELECT * FROM doctores WHERE idDoctor = :idDoctor")
    Doctor buscarPorId(int idDoctor);

}
