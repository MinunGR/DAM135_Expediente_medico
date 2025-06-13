package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entidad Room que representa un Doctor.
 */
@Entity(tableName = "doctores")
public class Doctor {

    @PrimaryKey(autoGenerate = true)
    private int idDoctor;

    private String nombre;
    private String especialidad;
    private String fotoUri;
    private String horariosDisponibles;

    /** Constructor vacío que usa Room */
    public Doctor() { }


    @Ignore
    public Doctor(String nombre, String especialidad, String fotoUri, String horariosDisponibles) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.fotoUri = fotoUri;
        this.horariosDisponibles = horariosDisponibles;
    }

    // Getters y setters

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public String getHorariosDisponibles() {
        return horariosDisponibles;
    }

    public void setHorariosDisponibles(String horariosDisponibles) {
        this.horariosDisponibles = horariosDisponibles;
    }


    @Override
    public String toString() {
        return nombre;
    }
}
