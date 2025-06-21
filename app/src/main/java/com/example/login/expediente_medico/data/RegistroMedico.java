package com.example.login.expediente_medico.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "registros_medicos")
public class RegistroMedico {
    // Autogenerable
    @PrimaryKey(autoGenerate = true)
    private int idRegistro;

    private int pacienteId;

    private long fechaRegistro;

    private String diagnostico;

    private String tratamiento;

    private String notas;

    private String fotoUri;

    @Ignore
    public RegistroMedico(int pacienteId, long fechaRegistro,
                          String diagnostico, String tratamiento,
                          String notas, String fotoUri)
    {
        this.pacienteId    = pacienteId;
        this.fechaRegistro = fechaRegistro;
        this.diagnostico   = diagnostico;
        this.tratamiento   = tratamiento;
        this.notas         = notas;
        this.fotoUri       = fotoUri;
    }

    public RegistroMedico() { }

    // Getters y setters
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }

    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public long getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(long fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getFotoUri() { return fotoUri; }
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }
}
