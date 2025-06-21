package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "consultorios")
public class Consultorio {
    // Autogenerable
    @PrimaryKey(autoGenerate = true)
    private int idConsultorio;

    private String nombre;

    private String ubicacion;

    @Ignore
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
