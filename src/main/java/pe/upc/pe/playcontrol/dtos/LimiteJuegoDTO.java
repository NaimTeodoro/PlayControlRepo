package pe.upc.pe.playcontrol.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Generated;

public class LimiteJuegoDTO {
    private Long idLimite;
    private Long idUsuario;
    private BigDecimal limiteHorasDiarias;
    private BigDecimal limiteHorasSemanal;
    private BigDecimal limiteHorasMensual;
    private LocalTime horaInicioDia;
    private LocalTime horaFinDia;
    private Boolean permitirFinDeSemana;
    private String diasPermitidos;
    private String metodoBloqueo;
    private String accionAlExceder;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaModificacion;
    private String observaciones;

    @Generated
    public LimiteJuegoDTO() {
    }

    @Generated
    public Long getIdLimite() {
        return this.idLimite;
    }

    @Generated
    public Long getIdUsuario() {
        return this.idUsuario;
    }

    @Generated
    public BigDecimal getLimiteHorasDiarias() {
        return this.limiteHorasDiarias;
    }

    @Generated
    public BigDecimal getLimiteHorasSemanal() {
        return this.limiteHorasSemanal;
    }

    @Generated
    public BigDecimal getLimiteHorasMensual() {
        return this.limiteHorasMensual;
    }

    @Generated
    public LocalTime getHoraInicioDia() {
        return this.horaInicioDia;
    }

    @Generated
    public LocalTime getHoraFinDia() {
        return this.horaFinDia;
    }

    @Generated
    public Boolean getPermitirFinDeSemana() {
        return this.permitirFinDeSemana;
    }

    @Generated
    public String getDiasPermitidos() {
        return this.diasPermitidos;
    }

    @Generated
    public String getMetodoBloqueo() {
        return this.metodoBloqueo;
    }

    @Generated
    public String getAccionAlExceder() {
        return this.accionAlExceder;
    }

    @Generated
    public String getEstado() {
        return this.estado;
    }

    @Generated
    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    @Generated
    public LocalDateTime getUltimaModificacion() {
        return this.ultimaModificacion;
    }

    @Generated
    public String getObservaciones() {
        return this.observaciones;
    }

    @Generated
    public void setIdLimite(final Long idLimite) {
        this.idLimite = idLimite;
    }

    @Generated
    public void setIdUsuario(final Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Generated
    public void setLimiteHorasDiarias(final BigDecimal limiteHorasDiarias) {
        this.limiteHorasDiarias = limiteHorasDiarias;
    }

    @Generated
    public void setLimiteHorasSemanal(final BigDecimal limiteHorasSemanal) {
        this.limiteHorasSemanal = limiteHorasSemanal;
    }

    @Generated
    public void setLimiteHorasMensual(final BigDecimal limiteHorasMensual) {
        this.limiteHorasMensual = limiteHorasMensual;
    }

    @Generated
    public void setHoraInicioDia(final LocalTime horaInicioDia) {
        this.horaInicioDia = horaInicioDia;
    }

    @Generated
    public void setHoraFinDia(final LocalTime horaFinDia) {
        this.horaFinDia = horaFinDia;
    }

    @Generated
    public void setPermitirFinDeSemana(final Boolean permitirFinDeSemana) {
        this.permitirFinDeSemana = permitirFinDeSemana;
    }

    @Generated
    public void setDiasPermitidos(final String diasPermitidos) {
        this.diasPermitidos = diasPermitidos;
    }

    @Generated
    public void setMetodoBloqueo(final String metodoBloqueo) {
        this.metodoBloqueo = metodoBloqueo;
    }

    @Generated
    public void setAccionAlExceder(final String accionAlExceder) {
        this.accionAlExceder = accionAlExceder;
    }

    @Generated
    public void setEstado(final String estado) {
        this.estado = estado;
    }

    @Generated
    public void setFechaCreacion(final LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Generated
    public void setUltimaModificacion(final LocalDateTime ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    @Generated
    public void setObservaciones(final String observaciones) {
        this.observaciones = observaciones;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof LimiteJuegoDTO)) {
            return false;
        } else {
            LimiteJuegoDTO other = (LimiteJuegoDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idLimite = this.getIdLimite();
                Object other$idLimite = other.getIdLimite();
                if (this$idLimite == null) {
                    if (other$idLimite != null) {
                        return false;
                    }
                } else if (!this$idLimite.equals(other$idLimite)) {
                    return false;
                }

                Object this$idUsuario = this.getIdUsuario();
                Object other$idUsuario = other.getIdUsuario();
                if (this$idUsuario == null) {
                    if (other$idUsuario != null) {
                        return false;
                    }
                } else if (!this$idUsuario.equals(other$idUsuario)) {
                    return false;
                }

                Object this$permitirFinDeSemana = this.getPermitirFinDeSemana();
                Object other$permitirFinDeSemana = other.getPermitirFinDeSemana();
                if (this$permitirFinDeSemana == null) {
                    if (other$permitirFinDeSemana != null) {
                        return false;
                    }
                } else if (!this$permitirFinDeSemana.equals(other$permitirFinDeSemana)) {
                    return false;
                }

                Object this$limiteHorasDiarias = this.getLimiteHorasDiarias();
                Object other$limiteHorasDiarias = other.getLimiteHorasDiarias();
                if (this$limiteHorasDiarias == null) {
                    if (other$limiteHorasDiarias != null) {
                        return false;
                    }
                } else if (!this$limiteHorasDiarias.equals(other$limiteHorasDiarias)) {
                    return false;
                }

                Object this$limiteHorasSemanal = this.getLimiteHorasSemanal();
                Object other$limiteHorasSemanal = other.getLimiteHorasSemanal();
                if (this$limiteHorasSemanal == null) {
                    if (other$limiteHorasSemanal != null) {
                        return false;
                    }
                } else if (!this$limiteHorasSemanal.equals(other$limiteHorasSemanal)) {
                    return false;
                }

                Object this$limiteHorasMensual = this.getLimiteHorasMensual();
                Object other$limiteHorasMensual = other.getLimiteHorasMensual();
                if (this$limiteHorasMensual == null) {
                    if (other$limiteHorasMensual != null) {
                        return false;
                    }
                } else if (!this$limiteHorasMensual.equals(other$limiteHorasMensual)) {
                    return false;
                }

                Object this$horaInicioDia = this.getHoraInicioDia();
                Object other$horaInicioDia = other.getHoraInicioDia();
                if (this$horaInicioDia == null) {
                    if (other$horaInicioDia != null) {
                        return false;
                    }
                } else if (!this$horaInicioDia.equals(other$horaInicioDia)) {
                    return false;
                }

                Object this$horaFinDia = this.getHoraFinDia();
                Object other$horaFinDia = other.getHoraFinDia();
                if (this$horaFinDia == null) {
                    if (other$horaFinDia != null) {
                        return false;
                    }
                } else if (!this$horaFinDia.equals(other$horaFinDia)) {
                    return false;
                }

                Object this$diasPermitidos = this.getDiasPermitidos();
                Object other$diasPermitidos = other.getDiasPermitidos();
                if (this$diasPermitidos == null) {
                    if (other$diasPermitidos != null) {
                        return false;
                    }
                } else if (!this$diasPermitidos.equals(other$diasPermitidos)) {
                    return false;
                }

                Object this$metodoBloqueo = this.getMetodoBloqueo();
                Object other$metodoBloqueo = other.getMetodoBloqueo();
                if (this$metodoBloqueo == null) {
                    if (other$metodoBloqueo != null) {
                        return false;
                    }
                } else if (!this$metodoBloqueo.equals(other$metodoBloqueo)) {
                    return false;
                }

                Object this$accionAlExceder = this.getAccionAlExceder();
                Object other$accionAlExceder = other.getAccionAlExceder();
                if (this$accionAlExceder == null) {
                    if (other$accionAlExceder != null) {
                        return false;
                    }
                } else if (!this$accionAlExceder.equals(other$accionAlExceder)) {
                    return false;
                }

                Object this$estado = this.getEstado();
                Object other$estado = other.getEstado();
                if (this$estado == null) {
                    if (other$estado != null) {
                        return false;
                    }
                } else if (!this$estado.equals(other$estado)) {
                    return false;
                }

                Object this$fechaCreacion = this.getFechaCreacion();
                Object other$fechaCreacion = other.getFechaCreacion();
                if (this$fechaCreacion == null) {
                    if (other$fechaCreacion != null) {
                        return false;
                    }
                } else if (!this$fechaCreacion.equals(other$fechaCreacion)) {
                    return false;
                }

                Object this$ultimaModificacion = this.getUltimaModificacion();
                Object other$ultimaModificacion = other.getUltimaModificacion();
                if (this$ultimaModificacion == null) {
                    if (other$ultimaModificacion != null) {
                        return false;
                    }
                } else if (!this$ultimaModificacion.equals(other$ultimaModificacion)) {
                    return false;
                }

                Object this$observaciones = this.getObservaciones();
                Object other$observaciones = other.getObservaciones();
                if (this$observaciones == null) {
                    if (other$observaciones != null) {
                        return false;
                    }
                } else if (!this$observaciones.equals(other$observaciones)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof LimiteJuegoDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idLimite = this.getIdLimite();
        result = result * 59 + ($idLimite == null ? 43 : $idLimite.hashCode());
        Object $idUsuario = this.getIdUsuario();
        result = result * 59 + ($idUsuario == null ? 43 : $idUsuario.hashCode());
        Object $permitirFinDeSemana = this.getPermitirFinDeSemana();
        result = result * 59 + ($permitirFinDeSemana == null ? 43 : $permitirFinDeSemana.hashCode());
        Object $limiteHorasDiarias = this.getLimiteHorasDiarias();
        result = result * 59 + ($limiteHorasDiarias == null ? 43 : $limiteHorasDiarias.hashCode());
        Object $limiteHorasSemanal = this.getLimiteHorasSemanal();
        result = result * 59 + ($limiteHorasSemanal == null ? 43 : $limiteHorasSemanal.hashCode());
        Object $limiteHorasMensual = this.getLimiteHorasMensual();
        result = result * 59 + ($limiteHorasMensual == null ? 43 : $limiteHorasMensual.hashCode());
        Object $horaInicioDia = this.getHoraInicioDia();
        result = result * 59 + ($horaInicioDia == null ? 43 : $horaInicioDia.hashCode());
        Object $horaFinDia = this.getHoraFinDia();
        result = result * 59 + ($horaFinDia == null ? 43 : $horaFinDia.hashCode());
        Object $diasPermitidos = this.getDiasPermitidos();
        result = result * 59 + ($diasPermitidos == null ? 43 : $diasPermitidos.hashCode());
        Object $metodoBloqueo = this.getMetodoBloqueo();
        result = result * 59 + ($metodoBloqueo == null ? 43 : $metodoBloqueo.hashCode());
        Object $accionAlExceder = this.getAccionAlExceder();
        result = result * 59 + ($accionAlExceder == null ? 43 : $accionAlExceder.hashCode());
        Object $estado = this.getEstado();
        result = result * 59 + ($estado == null ? 43 : $estado.hashCode());
        Object $fechaCreacion = this.getFechaCreacion();
        result = result * 59 + ($fechaCreacion == null ? 43 : $fechaCreacion.hashCode());
        Object $ultimaModificacion = this.getUltimaModificacion();
        result = result * 59 + ($ultimaModificacion == null ? 43 : $ultimaModificacion.hashCode());
        Object $observaciones = this.getObservaciones();
        result = result * 59 + ($observaciones == null ? 43 : $observaciones.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getIdLimite();
        return "LimiteJuegoDTO(idLimite=" + var10000 + ", idUsuario=" + this.getIdUsuario() + ", limiteHorasDiarias=" + String.valueOf(this.getLimiteHorasDiarias()) + ", limiteHorasSemanal=" + String.valueOf(this.getLimiteHorasSemanal()) + ", limiteHorasMensual=" + String.valueOf(this.getLimiteHorasMensual()) + ", horaInicioDia=" + String.valueOf(this.getHoraInicioDia()) + ", horaFinDia=" + String.valueOf(this.getHoraFinDia()) + ", permitirFinDeSemana=" + this.getPermitirFinDeSemana() + ", diasPermitidos=" + this.getDiasPermitidos() + ", metodoBloqueo=" + this.getMetodoBloqueo() + ", accionAlExceder=" + this.getAccionAlExceder() + ", estado=" + this.getEstado() + ", fechaCreacion=" + String.valueOf(this.getFechaCreacion()) + ", ultimaModificacion=" + String.valueOf(this.getUltimaModificacion()) + ", observaciones=" + this.getObservaciones() + ")";
    }
}
