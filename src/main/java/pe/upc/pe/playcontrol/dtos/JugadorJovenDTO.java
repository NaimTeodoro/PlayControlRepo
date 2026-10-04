package pe.upc.pe.playcontrol.dtos;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Generated;

public class JugadorJovenDTO {
    private Long idJugadorJoven;
    private Long idUsuario;
    private String nivelHabilidad;
    private String generoPreferido;
    private LocalTime horaPreferidaInicio;
    private LocalTime horaPreferidaFin;
    private String diasJuegoPreferidos;
    private String motivacionPrincipal;
    private String problemasIdentificados;
    private String historialProblemas;
    private LocalDateTime fechaRegistroSegmento;

    @Generated
    public JugadorJovenDTO() {
    }

    @Generated
    public Long getIdJugadorJoven() {
        return this.idJugadorJoven;
    }

    @Generated
    public Long getIdUsuario() {
        return this.idUsuario;
    }

    @Generated
    public String getNivelHabilidad() {
        return this.nivelHabilidad;
    }

    @Generated
    public String getGeneroPreferido() {
        return this.generoPreferido;
    }

    @Generated
    public LocalTime getHoraPreferidaInicio() {
        return this.horaPreferidaInicio;
    }

    @Generated
    public LocalTime getHoraPreferidaFin() {
        return this.horaPreferidaFin;
    }

    @Generated
    public String getDiasJuegoPreferidos() {
        return this.diasJuegoPreferidos;
    }

    @Generated
    public String getMotivacionPrincipal() {
        return this.motivacionPrincipal;
    }

    @Generated
    public String getProblemasIdentificados() {
        return this.problemasIdentificados;
    }

    @Generated
    public String getHistorialProblemas() {
        return this.historialProblemas;
    }

    @Generated
    public LocalDateTime getFechaRegistroSegmento() {
        return this.fechaRegistroSegmento;
    }

    @Generated
    public void setIdJugadorJoven(final Long idJugadorJoven) {
        this.idJugadorJoven = idJugadorJoven;
    }

    @Generated
    public void setIdUsuario(final Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Generated
    public void setNivelHabilidad(final String nivelHabilidad) {
        this.nivelHabilidad = nivelHabilidad;
    }

    @Generated
    public void setGeneroPreferido(final String generoPreferido) {
        this.generoPreferido = generoPreferido;
    }

    @Generated
    public void setHoraPreferidaInicio(final LocalTime horaPreferidaInicio) {
        this.horaPreferidaInicio = horaPreferidaInicio;
    }

    @Generated
    public void setHoraPreferidaFin(final LocalTime horaPreferidaFin) {
        this.horaPreferidaFin = horaPreferidaFin;
    }

    @Generated
    public void setDiasJuegoPreferidos(final String diasJuegoPreferidos) {
        this.diasJuegoPreferidos = diasJuegoPreferidos;
    }

    @Generated
    public void setMotivacionPrincipal(final String motivacionPrincipal) {
        this.motivacionPrincipal = motivacionPrincipal;
    }

    @Generated
    public void setProblemasIdentificados(final String problemasIdentificados) {
        this.problemasIdentificados = problemasIdentificados;
    }

    @Generated
    public void setHistorialProblemas(final String historialProblemas) {
        this.historialProblemas = historialProblemas;
    }

    @Generated
    public void setFechaRegistroSegmento(final LocalDateTime fechaRegistroSegmento) {
        this.fechaRegistroSegmento = fechaRegistroSegmento;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JugadorJovenDTO)) {
            return false;
        } else {
            JugadorJovenDTO other = (JugadorJovenDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idJugadorJoven = this.getIdJugadorJoven();
                Object other$idJugadorJoven = other.getIdJugadorJoven();
                if (this$idJugadorJoven == null) {
                    if (other$idJugadorJoven != null) {
                        return false;
                    }
                } else if (!this$idJugadorJoven.equals(other$idJugadorJoven)) {
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

                Object this$nivelHabilidad = this.getNivelHabilidad();
                Object other$nivelHabilidad = other.getNivelHabilidad();
                if (this$nivelHabilidad == null) {
                    if (other$nivelHabilidad != null) {
                        return false;
                    }
                } else if (!this$nivelHabilidad.equals(other$nivelHabilidad)) {
                    return false;
                }

                Object this$generoPreferido = this.getGeneroPreferido();
                Object other$generoPreferido = other.getGeneroPreferido();
                if (this$generoPreferido == null) {
                    if (other$generoPreferido != null) {
                        return false;
                    }
                } else if (!this$generoPreferido.equals(other$generoPreferido)) {
                    return false;
                }

                Object this$horaPreferidaInicio = this.getHoraPreferidaInicio();
                Object other$horaPreferidaInicio = other.getHoraPreferidaInicio();
                if (this$horaPreferidaInicio == null) {
                    if (other$horaPreferidaInicio != null) {
                        return false;
                    }
                } else if (!this$horaPreferidaInicio.equals(other$horaPreferidaInicio)) {
                    return false;
                }

                Object this$horaPreferidaFin = this.getHoraPreferidaFin();
                Object other$horaPreferidaFin = other.getHoraPreferidaFin();
                if (this$horaPreferidaFin == null) {
                    if (other$horaPreferidaFin != null) {
                        return false;
                    }
                } else if (!this$horaPreferidaFin.equals(other$horaPreferidaFin)) {
                    return false;
                }

                Object this$diasJuegoPreferidos = this.getDiasJuegoPreferidos();
                Object other$diasJuegoPreferidos = other.getDiasJuegoPreferidos();
                if (this$diasJuegoPreferidos == null) {
                    if (other$diasJuegoPreferidos != null) {
                        return false;
                    }
                } else if (!this$diasJuegoPreferidos.equals(other$diasJuegoPreferidos)) {
                    return false;
                }

                Object this$motivacionPrincipal = this.getMotivacionPrincipal();
                Object other$motivacionPrincipal = other.getMotivacionPrincipal();
                if (this$motivacionPrincipal == null) {
                    if (other$motivacionPrincipal != null) {
                        return false;
                    }
                } else if (!this$motivacionPrincipal.equals(other$motivacionPrincipal)) {
                    return false;
                }

                Object this$problemasIdentificados = this.getProblemasIdentificados();
                Object other$problemasIdentificados = other.getProblemasIdentificados();
                if (this$problemasIdentificados == null) {
                    if (other$problemasIdentificados != null) {
                        return false;
                    }
                } else if (!this$problemasIdentificados.equals(other$problemasIdentificados)) {
                    return false;
                }

                Object this$historialProblemas = this.getHistorialProblemas();
                Object other$historialProblemas = other.getHistorialProblemas();
                if (this$historialProblemas == null) {
                    if (other$historialProblemas != null) {
                        return false;
                    }
                } else if (!this$historialProblemas.equals(other$historialProblemas)) {
                    return false;
                }

                Object this$fechaRegistroSegmento = this.getFechaRegistroSegmento();
                Object other$fechaRegistroSegmento = other.getFechaRegistroSegmento();
                if (this$fechaRegistroSegmento == null) {
                    if (other$fechaRegistroSegmento != null) {
                        return false;
                    }
                } else if (!this$fechaRegistroSegmento.equals(other$fechaRegistroSegmento)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof JugadorJovenDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idJugadorJoven = this.getIdJugadorJoven();
        result = result * 59 + ($idJugadorJoven == null ? 43 : $idJugadorJoven.hashCode());
        Object $idUsuario = this.getIdUsuario();
        result = result * 59 + ($idUsuario == null ? 43 : $idUsuario.hashCode());
        Object $nivelHabilidad = this.getNivelHabilidad();
        result = result * 59 + ($nivelHabilidad == null ? 43 : $nivelHabilidad.hashCode());
        Object $generoPreferido = this.getGeneroPreferido();
        result = result * 59 + ($generoPreferido == null ? 43 : $generoPreferido.hashCode());
        Object $horaPreferidaInicio = this.getHoraPreferidaInicio();
        result = result * 59 + ($horaPreferidaInicio == null ? 43 : $horaPreferidaInicio.hashCode());
        Object $horaPreferidaFin = this.getHoraPreferidaFin();
        result = result * 59 + ($horaPreferidaFin == null ? 43 : $horaPreferidaFin.hashCode());
        Object $diasJuegoPreferidos = this.getDiasJuegoPreferidos();
        result = result * 59 + ($diasJuegoPreferidos == null ? 43 : $diasJuegoPreferidos.hashCode());
        Object $motivacionPrincipal = this.getMotivacionPrincipal();
        result = result * 59 + ($motivacionPrincipal == null ? 43 : $motivacionPrincipal.hashCode());
        Object $problemasIdentificados = this.getProblemasIdentificados();
        result = result * 59 + ($problemasIdentificados == null ? 43 : $problemasIdentificados.hashCode());
        Object $historialProblemas = this.getHistorialProblemas();
        result = result * 59 + ($historialProblemas == null ? 43 : $historialProblemas.hashCode());
        Object $fechaRegistroSegmento = this.getFechaRegistroSegmento();
        result = result * 59 + ($fechaRegistroSegmento == null ? 43 : $fechaRegistroSegmento.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getIdJugadorJoven();
        return "JugadorJovenDTO(idJugadorJoven=" + var10000 + ", idUsuario=" + this.getIdUsuario() + ", nivelHabilidad=" + this.getNivelHabilidad() + ", generoPreferido=" + this.getGeneroPreferido() + ", horaPreferidaInicio=" + String.valueOf(this.getHoraPreferidaInicio()) + ", horaPreferidaFin=" + String.valueOf(this.getHoraPreferidaFin()) + ", diasJuegoPreferidos=" + this.getDiasJuegoPreferidos() + ", motivacionPrincipal=" + this.getMotivacionPrincipal() + ", problemasIdentificados=" + this.getProblemasIdentificados() + ", historialProblemas=" + this.getHistorialProblemas() + ", fechaRegistroSegmento=" + String.valueOf(this.getFechaRegistroSegmento()) + ")";
    }
}
