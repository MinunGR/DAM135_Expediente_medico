package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "citas")
public class Cita {

    // Autogenerable
    @PrimaryKey(autoGenerate = true)
    private int idCita;
    private int doctorId;
    private int pacienteId;
    private long fechaHora;
    private String motivo;
    @Ignore
    public Cita(int doctorId, int pacienteId, long fechaHora, String motivo) {
        this.doctorId   = doctorId;
        this.pacienteId = pacienteId;
        this.fechaHora  = fechaHora;
        this.motivo     = motivo;
    }

    public Cita() {
    }

    // Getters y setters
    public int getIdCita() {
        return idCita;
    }
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public int getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(int pacienteId) {
        this.pacienteId = pacienteId;
    }
    public long getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(long fechaHora) {
        this.fechaHora = fechaHora;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
