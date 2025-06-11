package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Para operaciones CRUD sobre la entidad
@Dao
public interface CitaDao {

    // Inserta una nueva cita
    @Insert
    long insertarCita(Cita cita);

    // Actualizar los campos
    @Update
    int actualizarCita(Cita cita);

    // Eliminar una cita
    @Delete
    int eliminarCita(Cita cita);

    // Obtener todos
    @Query("SELECT * FROM citas")
    List<Cita> obtenerCitas();

    // Busca una cita por el id
    @Query("SELECT * FROM citas WHERE idCita = :idCita")
    Cita buscarCitaPorId(int idCita);

    // Citas futuras cuando fechaHora >= ahora)
    @Query("SELECT * FROM citas WHERE fechaHora >= :ahora ORDER BY fechaHora ASC")
    List<Cita> obtenerCitasProximas(long ahora);

    // Citas pasadas (fechaHora < ahora)
    @Query("SELECT * FROM citas WHERE fechaHora < :ahora ORDER BY fechaHora DESC")
    List<Cita> obtenerCitasPasadas(long ahora);
}
