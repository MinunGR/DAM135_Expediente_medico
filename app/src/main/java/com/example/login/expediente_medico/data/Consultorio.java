package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entidad consultorios
@Entity(tableName = "consultorios")
public class Consultorio {
    // Campos

    // Id que se autogenera
    @PrimaryKey(autoGenerate = true)
    private int idConsultorio;

    private String nombre; // Nombre o número de consultorio (ej: sala 1)

    private String ubicacion; // Ubicación o dirección dentro del centro médico

    // Constructor
    public Consultorio(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Consultorio() {
    }

    // Getters y setters
    public int getIdConsultorio() {
        return idConsultorio;
    }
    public void setIdConsultorio(int idConsultorio) {
        this.idConsultorio = idConsultorio;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
