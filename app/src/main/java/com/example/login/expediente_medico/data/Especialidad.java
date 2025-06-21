package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "especialidades")
public class Especialidad {
    // Autogenerable
    @PrimaryKey(autoGenerate = true)
    private int idEspecialidad;
    private String nombre;

    @Ignore
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
