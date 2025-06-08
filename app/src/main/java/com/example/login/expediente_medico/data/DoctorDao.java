package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Para operaciones CRUD sobre la entidad
@Dao
public interface DoctorDao {

    // Insertar un Doctor
    @Insert
    long insertarDoctor(Doctor doctor);

   // Obtener todos
    @Query("SELECT * FROM doctores")
    List<Doctor> obtenerDoctores();

   // Actualizar los campos
    @Update
    int actualizarDoctor(Doctor doctor);

    // Eliminar un ductor
    @Delete
    int eliminarDoctor(Doctor doctor);

    // Busca un doctor por el id
    @Query("SELECT * FROM doctores WHERE idDoctor = :idDoctor")
    Doctor buscarDoctorPorId(int idDoctor);

}
