package pe.upc.pe.playcontrol.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profesional_salud")
public class ProfesionalSalud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional")
    private Integer idProfesional;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "especialidad", length = 150, nullable = false)
    private String especialidad;

    @Column(name = "numero_licencia", length = 100, nullable = false)
    private String numeroLicencia;

    @Column(name = "institucion", length = 200)
    private String institucion;

    @Column(name = "anios_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "area_trabajo", length = 50)
    private String areaTrabajo;

    @Column(name = "certificaciones", columnDefinition = "TEXT")
    private String certificaciones;

    @Column(name = "telefono_consulta", length = 20)
    private String telefonoConsulta;

    @Column(name = "horario_consulta", length = 200)
    private String horarioConsulta;

    @Column(name = "verificado_por", length = 100)
    private String verificadoPor;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    public ProfesionalSalud() {
    }

    public Integer getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(Integer idProfesional) {
        this.idProfesional = idProfesional;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public Integer getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(Integer aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getAreaTrabajo() {
        return areaTrabajo;
    }

    public void setAreaTrabajo(String areaTrabajo) {
        this.areaTrabajo = areaTrabajo;
    }

    public String getCertificaciones() {
        return certificaciones;
    }

    public void setCertificaciones(String certificaciones) {
        this.certificaciones = certificaciones;
    }

    public String getTelefonoConsulta() {
        return telefonoConsulta;
    }

    public void setTelefonoConsulta(String telefonoConsulta) {
        this.telefonoConsulta = telefonoConsulta;
    }

    public String getHorarioConsulta() {
        return horarioConsulta;
    }

    public void setHorarioConsulta(String horarioConsulta) {
        this.horarioConsulta = horarioConsulta;
    }

    public String getVerificadoPor() {
        return verificadoPor;
    }

    public void setVerificadoPor(String verificadoPor) {
        this.verificadoPor = verificadoPor;
    }

    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }

    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
