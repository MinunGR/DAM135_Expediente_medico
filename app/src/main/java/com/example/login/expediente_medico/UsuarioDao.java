package com.example.login.expediente_medico;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * DAO para operaciones CRUD sobre la entidad Usuario.
 */
@Dao
public interface UsuarioDao {

    /**
     * Inserta un nuevo usuario.
     * @param u Entidad Usuario a insertar.
     * @return ID generado (>=1) si tuvo éxito.
     */
    @Insert
    long insertar(Usuario u);

    /**
     * Busca un usuario por su correo.
     * @param email Correo a buscar.
     * @return Usuario si existe, o null.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :email LIMIT 1")
    Usuario buscarPorCorreo(String email);
}
