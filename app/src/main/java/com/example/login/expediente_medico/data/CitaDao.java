package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 * Interfaz Data Access Object (DAO) para operaciones CRUD de la entidad Cita.
 * Proporciona métodos para gestionar consultas en la base de datos.
 */
@Dao
public interface CitaDao {
    // Inserta en db
    @Insert
    long insertarCita(Cita cita);
    // Actualiza en db
    @Update
    int actualizarDatosCita(Cita cita);
    // Elimina en db
    @Delete
    int eliminarCita(Cita cita);
    // Obtiene todos
    @Query("SELECT * FROM citas")
    List<Cita> obtenerCitas();
    // Busca por id
    @Query("SELECT * FROM citas WHERE idCita = :idCita")
    Cita buscarCitaPorId(int idCita);
    // Obtiene registros futuros
    @Query("SELECT * FROM citas WHERE fechaHora >= :hora ORDER BY fechaHora ASC")
    List<Cita> listarFuturasOrdenadas(long hora);
    // Obtieen registros historicos
    @Query("SELECT * FROM citas WHERE fechaHora < :hora ORDER BY fechaHora DESC")
    List<Cita> listarHistoricasRecientes(long hora);
}
