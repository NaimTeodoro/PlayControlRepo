package pe.upc.pe.playcontrol.dtos;

import java.time.LocalDateTime;
import lombok.Generated;

public class PadreTutorDTO {
    private Long idPadreTutor;
    private Long idUsuario;
    private Integer numeroHijos;
    private String hijosMonitoreados;
    private String metodosControlActual;
    private String preocupacionesPrincipales;
    private String nivelTecnologia;
    private String objetivosSupervision;
    private String frecuenciaRevisionReportes;
    private String configuracionAlertas;
    private LocalDateTime fechaRegistro;

    @Generated
    public PadreTutorDTO() {
    }

    @Generated
    public Long getIdPadreTutor() {
        return this.idPadreTutor;
    }

    @Generated
    public Long getIdUsuario() {
        return this.idUsuario;
    }

    @Generated
    public Integer getNumeroHijos() {
        return this.numeroHijos;
    }

    @Generated
    public String getHijosMonitoreados() {
        return this.hijosMonitoreados;
    }

    @Generated
    public String getMetodosControlActual() {
        return this.metodosControlActual;
    }

    @Generated
    public String getPreocupacionesPrincipales() {
        return this.preocupacionesPrincipales;
    }

    @Generated
    public String getNivelTecnologia() {
        return this.nivelTecnologia;
    }

    @Generated
    public String getObjetivosSupervision() {
        return this.objetivosSupervision;
    }

    @Generated
    public String getFrecuenciaRevisionReportes() {
        return this.frecuenciaRevisionReportes;
    }

    @Generated
    public String getConfiguracionAlertas() {
        return this.configuracionAlertas;
    }

    @Generated
    public LocalDateTime getFechaRegistro() {
        return this.fechaRegistro;
    }

    @Generated
    public void setIdPadreTutor(final Long idPadreTutor) {
        this.idPadreTutor = idPadreTutor;
    }

    @Generated
    public void setIdUsuario(final Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Generated
    public void setNumeroHijos(final Integer numeroHijos) {
        this.numeroHijos = numeroHijos;
    }

    @Generated
    public void setHijosMonitoreados(final String hijosMonitoreados) {
        this.hijosMonitoreados = hijosMonitoreados;
    }

    @Generated
    public void setMetodosControlActual(final String metodosControlActual) {
        this.metodosControlActual = metodosControlActual;
    }

    @Generated
    public void setPreocupacionesPrincipales(final String preocupacionesPrincipales) {
        this.preocupacionesPrincipales = preocupacionesPrincipales;
    }

    @Generated
    public void setNivelTecnologia(final String nivelTecnologia) {
        this.nivelTecnologia = nivelTecnologia;
    }

    @Generated
    public void setObjetivosSupervision(final String objetivosSupervision) {
        this.objetivosSupervision = objetivosSupervision;
    }

    @Generated
    public void setFrecuenciaRevisionReportes(final String frecuenciaRevisionReportes) {
        this.frecuenciaRevisionReportes = frecuenciaRevisionReportes;
    }

    @Generated
    public void setConfiguracionAlertas(final String configuracionAlertas) {
        this.configuracionAlertas = configuracionAlertas;
    }

    @Generated
    public void setFechaRegistro(final LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PadreTutorDTO)) {
            return false;
        } else {
            PadreTutorDTO other = (PadreTutorDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idPadreTutor = this.getIdPadreTutor();
                Object other$idPadreTutor = other.getIdPadreTutor();
                if (this$idPadreTutor == null) {
                    if (other$idPadreTutor != null) {
                        return false;
                    }
                } else if (!this$idPadreTutor.equals(other$idPadreTutor)) {
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

                Object this$numeroHijos = this.getNumeroHijos();
                Object other$numeroHijos = other.getNumeroHijos();
                if (this$numeroHijos == null) {
                    if (other$numeroHijos != null) {
                        return false;
                    }
                } else if (!this$numeroHijos.equals(other$numeroHijos)) {
                    return false;
                }

                Object this$hijosMonitoreados = this.getHijosMonitoreados();
                Object other$hijosMonitoreados = other.getHijosMonitoreados();
                if (this$hijosMonitoreados == null) {
                    if (other$hijosMonitoreados != null) {
                        return false;
                    }
                } else if (!this$hijosMonitoreados.equals(other$hijosMonitoreados)) {
                    return false;
                }

                Object this$metodosControlActual = this.getMetodosControlActual();
                Object other$metodosControlActual = other.getMetodosControlActual();
                if (this$metodosControlActual == null) {
                    if (other$metodosControlActual != null) {
                        return false;
                    }
                } else if (!this$metodosControlActual.equals(other$metodosControlActual)) {
                    return false;
                }

                Object this$preocupacionesPrincipales = this.getPreocupacionesPrincipales();
                Object other$preocupacionesPrincipales = other.getPreocupacionesPrincipales();
                if (this$preocupacionesPrincipales == null) {
                    if (other$preocupacionesPrincipales != null) {
                        return false;
                    }
                } else if (!this$preocupacionesPrincipales.equals(other$preocupacionesPrincipales)) {
                    return false;
                }

                Object this$nivelTecnologia = this.getNivelTecnologia();
                Object other$nivelTecnologia = other.getNivelTecnologia();
                if (this$nivelTecnologia == null) {
                    if (other$nivelTecnologia != null) {
                        return false;
                    }
                } else if (!this$nivelTecnologia.equals(other$nivelTecnologia)) {
                    return false;
                }

                Object this$objetivosSupervision = this.getObjetivosSupervision();
                Object other$objetivosSupervision = other.getObjetivosSupervision();
                if (this$objetivosSupervision == null) {
                    if (other$objetivosSupervision != null) {
                        return false;
                    }
                } else if (!this$objetivosSupervision.equals(other$objetivosSupervision)) {
                    return false;
                }

                Object this$frecuenciaRevisionReportes = this.getFrecuenciaRevisionReportes();
                Object other$frecuenciaRevisionReportes = other.getFrecuenciaRevisionReportes();
                if (this$frecuenciaRevisionReportes == null) {
                    if (other$frecuenciaRevisionReportes != null) {
                        return false;
                    }
                } else if (!this$frecuenciaRevisionReportes.equals(other$frecuenciaRevisionReportes)) {
                    return false;
                }

                Object this$configuracionAlertas = this.getConfiguracionAlertas();
                Object other$configuracionAlertas = other.getConfiguracionAlertas();
                if (this$configuracionAlertas == null) {
                    if (other$configuracionAlertas != null) {
                        return false;
                    }
                } else if (!this$configuracionAlertas.equals(other$configuracionAlertas)) {
                    return false;
                }

                Object this$fechaRegistro = this.getFechaRegistro();
                Object other$fechaRegistro = other.getFechaRegistro();
                if (this$fechaRegistro == null) {
                    if (other$fechaRegistro != null) {
                        return false;
                    }
                } else if (!this$fechaRegistro.equals(other$fechaRegistro)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof PadreTutorDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idPadreTutor = this.getIdPadreTutor();
        result = result * 59 + ($idPadreTutor == null ? 43 : $idPadreTutor.hashCode());
        Object $idUsuario = this.getIdUsuario();
        result = result * 59 + ($idUsuario == null ? 43 : $idUsuario.hashCode());
        Object $numeroHijos = this.getNumeroHijos();
        result = result * 59 + ($numeroHijos == null ? 43 : $numeroHijos.hashCode());
        Object $hijosMonitoreados = this.getHijosMonitoreados();
        result = result * 59 + ($hijosMonitoreados == null ? 43 : $hijosMonitoreados.hashCode());
        Object $metodosControlActual = this.getMetodosControlActual();
        result = result * 59 + ($metodosControlActual == null ? 43 : $metodosControlActual.hashCode());
        Object $preocupacionesPrincipales = this.getPreocupacionesPrincipales();
        result = result * 59 + ($preocupacionesPrincipales == null ? 43 : $preocupacionesPrincipales.hashCode());
        Object $nivelTecnologia = this.getNivelTecnologia();
        result = result * 59 + ($nivelTecnologia == null ? 43 : $nivelTecnologia.hashCode());
        Object $objetivosSupervision = this.getObjetivosSupervision();
        result = result * 59 + ($objetivosSupervision == null ? 43 : $objetivosSupervision.hashCode());
        Object $frecuenciaRevisionReportes = this.getFrecuenciaRevisionReportes();
        result = result * 59 + ($frecuenciaRevisionReportes == null ? 43 : $frecuenciaRevisionReportes.hashCode());
        Object $configuracionAlertas = this.getConfiguracionAlertas();
        result = result * 59 + ($configuracionAlertas == null ? 43 : $configuracionAlertas.hashCode());
        Object $fechaRegistro = this.getFechaRegistro();
        result = result * 59 + ($fechaRegistro == null ? 43 : $fechaRegistro.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getIdPadreTutor();
        return "PadreTutorDTO(idPadreTutor=" + var10000 + ", idUsuario=" + this.getIdUsuario() + ", numeroHijos=" + this.getNumeroHijos() + ", hijosMonitoreados=" + this.getHijosMonitoreados() + ", metodosControlActual=" + this.getMetodosControlActual() + ", preocupacionesPrincipales=" + this.getPreocupacionesPrincipales() + ", nivelTecnologia=" + this.getNivelTecnologia() + ", objetivosSupervision=" + this.getObjetivosSupervision() + ", frecuenciaRevisionReportes=" + this.getFrecuenciaRevisionReportes() + ", configuracionAlertas=" + this.getConfiguracionAlertas() + ", fechaRegistro=" + String.valueOf(this.getFechaRegistro()) + ")";
    }
}
