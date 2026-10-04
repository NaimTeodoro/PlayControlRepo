package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profesional_cliente")
public class ProfesionalCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional_cliente")
    private Integer idProfesionalCliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_profesional", nullable = false)
    private ProfesionalSalud profesional;

    @Column(name = "id_jugador_joven", nullable = false)
    private Integer idJugadorJoven;

    @Column(name = "fecha_asociacion", nullable = false)
    private LocalDateTime fechaAsociacion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "plan_tratamiento", columnDefinition = "TEXT")
    private String planTratamiento;

    @Column(name = "frecuencia_consulta", length = 100)
    private String frecuenciaConsulta;

    @Column(name = "proxima_consulta")
    private LocalDateTime proximaConsulta;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    public ProfesionalCliente() {
    }

    public Integer getIdProfesionalCliente() {
        return idProfesionalCliente;
    }

    public void setIdProfesionalCliente(Integer idProfesionalCliente) {
        this.idProfesionalCliente = idProfesionalCliente;
    }

    public ProfesionalSalud getProfesional() {
        return profesional;
    }

    public void setProfesional(ProfesionalSalud profesional) {
        this.profesional = profesional;
    }

    public Integer getIdJugadorJoven() {
        return idJugadorJoven;
    }

    public void setIdJugadorJoven(Integer idJugadorJoven) {
        this.idJugadorJoven = idJugadorJoven;
    }

    public LocalDateTime getFechaAsociacion() {
        return fechaAsociacion;
    }

    public void setFechaAsociacion(LocalDateTime fechaAsociacion) {
        this.fechaAsociacion = fechaAsociacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPlanTratamiento() {
        return planTratamiento;
    }

    public void setPlanTratamiento(String planTratamiento) {
        this.planTratamiento = planTratamiento;
    }

    public String getFrecuenciaConsulta() {
        return frecuenciaConsulta;
    }

    public void setFrecuenciaConsulta(String frecuenciaConsulta) {
        this.frecuenciaConsulta = frecuenciaConsulta;
    }

    public LocalDateTime getProximaConsulta() {
        return proximaConsulta;
    }

    public void setProximaConsulta(LocalDateTime proximaConsulta) {
        this.proximaConsulta = proximaConsulta;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
