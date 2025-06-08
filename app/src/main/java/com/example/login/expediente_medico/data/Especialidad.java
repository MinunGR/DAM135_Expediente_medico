package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidad especialidad
@Entity(tableName = "especialidades")
public class Especialidad {
    // Campos

    // Id que se autogenera
    @PrimaryKey(autoGenerate = true)
    private int idEspecialidad;

    private String nombre;

    // Constructor
    public Especialidad(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad() {
    }

    // Getters y setters
    public int getIdEspecialidad() {
        return idEspecialidad;
    }
    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
