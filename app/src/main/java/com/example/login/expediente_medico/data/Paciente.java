package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pacientes")
public class Paciente {
    // Autogenerable
    @PrimaryKey(autoGenerate = true)
    private int idPaciente;

    private String nombre;

    private String datosContacto;

    private String fotoUri;

    @Ignore
    public Paciente(String nombre, String datosContacto, String fotoUri) {
        this.nombre = nombre;
        this.datosContacto = datosContacto;
        this.fotoUri = fotoUri;
    }

    public Paciente() {
    }

    // Getters y setters
    public int getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDatosContacto() {
        return datosContacto;
    }
    public void setDatosContacto(String datosContacto) {
        this.datosContacto = datosContacto;
    }
    public String getFotoUri() {
        return fotoUri;
    }
    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
