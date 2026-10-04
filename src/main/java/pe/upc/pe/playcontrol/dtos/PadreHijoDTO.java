package pe.upc.pe.playcontrol.dtos;

import java.time.LocalDateTime;
import lombok.Generated;

public class PadreHijoDTO {
    private Long idPadreHijo;
    private Long idPadreTutor;
    private Long idJugadorJoven;
    private String tipoRelacion;
    private LocalDateTime fechaAsociacion;
    private String estado;
    private String permisosSupervision;
    private Boolean autorizado;
    private String codigoAcceso;

    @Generated
    public PadreHijoDTO() {
    }

    @Generated
    public Long getIdPadreHijo() {
        return this.idPadreHijo;
    }

    @Generated
    public Long getIdPadreTutor() {
        return this.idPadreTutor;
    }

    @Generated
    public Long getIdJugadorJoven() {
        return this.idJugadorJoven;
    }

    @Generated
    public String getTipoRelacion() {
        return this.tipoRelacion;
    }

    @Generated
    public LocalDateTime getFechaAsociacion() {
        return this.fechaAsociacion;
    }

    @Generated
    public String getEstado() {
        return this.estado;
    }

    @Generated
    public String getPermisosSupervision() {
        return this.permisosSupervision;
    }

    @Generated
    public Boolean getAutorizado() {
        return this.autorizado;
    }

    @Generated
    public String getCodigoAcceso() {
        return this.codigoAcceso;
    }

    @Generated
    public void setIdPadreHijo(final Long idPadreHijo) {
        this.idPadreHijo = idPadreHijo;
    }

    @Generated
    public void setIdPadreTutor(final Long idPadreTutor) {
        this.idPadreTutor = idPadreTutor;
    }

    @Generated
    public void setIdJugadorJoven(final Long idJugadorJoven) {
        this.idJugadorJoven = idJugadorJoven;
    }

    @Generated
    public void setTipoRelacion(final String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    @Generated
    public void setFechaAsociacion(final LocalDateTime fechaAsociacion) {
        this.fechaAsociacion = fechaAsociacion;
    }

    @Generated
    public void setEstado(final String estado) {
        this.estado = estado;
    }

    @Generated
    public void setPermisosSupervision(final String permisosSupervision) {
        this.permisosSupervision = permisosSupervision;
    }

    @Generated
    public void setAutorizado(final Boolean autorizado) {
        this.autorizado = autorizado;
    }

    @Generated
    public void setCodigoAcceso(final String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PadreHijoDTO)) {
            return false;
        } else {
            PadreHijoDTO other = (PadreHijoDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idPadreHijo = this.getIdPadreHijo();
                Object other$idPadreHijo = other.getIdPadreHijo();
                if (this$idPadreHijo == null) {
                    if (other$idPadreHijo != null) {
                        return false;
                    }
                } else if (!this$idPadreHijo.equals(other$idPadreHijo)) {
                    return false;
                }

                Object this$idPadreTutor = this.getIdPadreTutor();
                Object other$idPadreTutor = other.getIdPadreTutor();
                if (this$idPadreTutor == null) {
                    if (other$idPadreTutor != null) {
                        return false;
                    }
                } else if (!this$idPadreTutor.equals(other$idPadreTutor)) {
                    return false;
                }

                Object this$idJugadorJoven = this.getIdJugadorJoven();
                Object other$idJugadorJoven = other.getIdJugadorJoven();
                if (this$idJugadorJoven == null) {
                    if (other$idJugadorJoven != null) {
                        return false;
                    }
                } else if (!this$idJugadorJoven.equals(other$idJugadorJoven)) {
                    return false;
                }

                Object this$autorizado = this.getAutorizado();
                Object other$autorizado = other.getAutorizado();
                if (this$autorizado == null) {
                    if (other$autorizado != null) {
                        return false;
                    }
                } else if (!this$autorizado.equals(other$autorizado)) {
                    return false;
                }

                Object this$tipoRelacion = this.getTipoRelacion();
                Object other$tipoRelacion = other.getTipoRelacion();
                if (this$tipoRelacion == null) {
                    if (other$tipoRelacion != null) {
                        return false;
                    }
                } else if (!this$tipoRelacion.equals(other$tipoRelacion)) {
                    return false;
                }

                Object this$fechaAsociacion = this.getFechaAsociacion();
                Object other$fechaAsociacion = other.getFechaAsociacion();
                if (this$fechaAsociacion == null) {
                    if (other$fechaAsociacion != null) {
                        return false;
                    }
                } else if (!this$fechaAsociacion.equals(other$fechaAsociacion)) {
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

                Object this$permisosSupervision = this.getPermisosSupervision();
                Object other$permisosSupervision = other.getPermisosSupervision();
                if (this$permisosSupervision == null) {
                    if (other$permisosSupervision != null) {
                        return false;
                    }
                } else if (!this$permisosSupervision.equals(other$permisosSupervision)) {
                    return false;
                }

                Object this$codigoAcceso = this.getCodigoAcceso();
                Object other$codigoAcceso = other.getCodigoAcceso();
                if (this$codigoAcceso == null) {
                    if (other$codigoAcceso != null) {
                        return false;
                    }
                } else if (!this$codigoAcceso.equals(other$codigoAcceso)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof PadreHijoDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idPadreHijo = this.getIdPadreHijo();
        result = result * 59 + ($idPadreHijo == null ? 43 : $idPadreHijo.hashCode());
        Object $idPadreTutor = this.getIdPadreTutor();
        result = result * 59 + ($idPadreTutor == null ? 43 : $idPadreTutor.hashCode());
        Object $idJugadorJoven = this.getIdJugadorJoven();
        result = result * 59 + ($idJugadorJoven == null ? 43 : $idJugadorJoven.hashCode());
        Object $autorizado = this.getAutorizado();
        result = result * 59 + ($autorizado == null ? 43 : $autorizado.hashCode());
        Object $tipoRelacion = this.getTipoRelacion();
        result = result * 59 + ($tipoRelacion == null ? 43 : $tipoRelacion.hashCode());
        Object $fechaAsociacion = this.getFechaAsociacion();
        result = result * 59 + ($fechaAsociacion == null ? 43 : $fechaAsociacion.hashCode());
        Object $estado = this.getEstado();
        result = result * 59 + ($estado == null ? 43 : $estado.hashCode());
        Object $permisosSupervision = this.getPermisosSupervision();
        result = result * 59 + ($permisosSupervision == null ? 43 : $permisosSupervision.hashCode());
        Object $codigoAcceso = this.getCodigoAcceso();
        result = result * 59 + ($codigoAcceso == null ? 43 : $codigoAcceso.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getIdPadreHijo();
        return "PadreHijoDTO(idPadreHijo=" + var10000 + ", idPadreTutor=" + this.getIdPadreTutor() + ", idJugadorJoven=" + this.getIdJugadorJoven() + ", tipoRelacion=" + this.getTipoRelacion() + ", fechaAsociacion=" + String.valueOf(this.getFechaAsociacion()) + ", estado=" + this.getEstado() + ", permisosSupervision=" + this.getPermisosSupervision() + ", autorizado=" + this.getAutorizado() + ", codigoAcceso=" + this.getCodigoAcceso() + ")";
    }
}
