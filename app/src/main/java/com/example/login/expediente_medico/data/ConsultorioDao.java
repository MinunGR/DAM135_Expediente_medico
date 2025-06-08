package com.example.login.expediente_medico.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Para operaciones CRUD sobre la entidad
public interface ConsultorioDao {

    // Insertar un consultorio
    @Insert
    long insertarConsultorio(Consultorio consultorio);

    // Obtener todos
    @Query("SELECT * FROM consultorios")
    List<Consultorio> obtenerConsultorios();

    // Actualizar los campos
    @Update
    int actualizarConsultorio(Consultorio consultorio);

    // Eliminar un consultorio

    @Delete
    int eliminarConsultorio(Consultorio consultorio);

    // Busca un consultorio por el id
    @Query("SELECT * FROM consultorios WHERE idConsultorio = :idConsultorio")
    Consultorio buscarConsultorioPorId(int idConsultorio);

}
