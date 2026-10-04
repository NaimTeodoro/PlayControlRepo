package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(
        name = "padre_hijo"
)
public class PadreHijo {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_padre_hijo"
    )
    private Long idPadreHijo;
    @ManyToOne
    @JoinColumn(
            name = "id_padre_tutor",
            nullable = false
    )
    private PadreTutor padreTutor;
    @ManyToOne
    @JoinColumn(
            name = "id_jugador_joven",
            nullable = false
    )
    private JugadorJoven jugadorJoven;
    @Column(
            name = "tipo_relacion",
            length = 30
    )
    private String tipoRelacion;
    @Column(
            name = "fecha_asociacion"
    )
    private LocalDateTime fechaAsociacion;
    @Column(
            name = "estado",
            length = 20
    )
    private String estado;
    @Column(
            name = "permisos_supervision",
            length = 500
    )
    private String permisosSupervision;
    @Column(
            name = "autorizado"
    )
    private Boolean autorizado;
    @Column(
            name = "codigo_acceso",
            length = 50
    )
    private String codigoAcceso;

    @Generated
    public PadreHijo() {
    }

    @Generated
    public Long getIdPadreHijo() {
        return this.idPadreHijo;
    }

    @Generated
    public PadreTutor getPadreTutor() {
        return this.padreTutor;
    }

    @Generated
    public JugadorJoven getJugadorJoven() {
        return this.jugadorJoven;
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
    public void setPadreTutor(final PadreTutor padreTutor) {
        this.padreTutor = padreTutor;
    }

    @Generated
    public void setJugadorJoven(final JugadorJoven jugadorJoven) {
        this.jugadorJoven = jugadorJoven;
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
        } else if (!(o instanceof PadreHijo)) {
            return false;
        } else {
            PadreHijo other = (PadreHijo)o;
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

                Object this$autorizado = this.getAutorizado();
                Object other$autorizado = other.getAutorizado();
                if (this$autorizado == null) {
                    if (other$autorizado != null) {
                        return false;
                    }
                } else if (!this$autorizado.equals(other$autorizado)) {
                    return false;
                }

                Object this$padreTutor = this.getPadreTutor();
                Object other$padreTutor = other.getPadreTutor();
                if (this$padreTutor == null) {
                    if (other$padreTutor != null) {
                        return false;
                    }
                } else if (!this$padreTutor.equals(other$padreTutor)) {
                    return false;
                }

                Object this$jugadorJoven = this.getJugadorJoven();
                Object other$jugadorJoven = other.getJugadorJoven();
                if (this$jugadorJoven == null) {
                    if (other$jugadorJoven != null) {
                        return false;
                    }
                } else if (!this$jugadorJoven.equals(other$jugadorJoven)) {
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
        return other instanceof PadreHijo;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idPadreHijo = this.getIdPadreHijo();
        result = result * 59 + ($idPadreHijo == null ? 43 : $idPadreHijo.hashCode());
        Object $autorizado = this.getAutorizado();
        result = result * 59 + ($autorizado == null ? 43 : $autorizado.hashCode());
        Object $padreTutor = this.getPadreTutor();
        result = result * 59 + ($padreTutor == null ? 43 : $padreTutor.hashCode());
        Object $jugadorJoven = this.getJugadorJoven();
        result = result * 59 + ($jugadorJoven == null ? 43 : $jugadorJoven.hashCode());
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
        return "PadreHijo(idPadreHijo=" + var10000 + ", padreTutor=" + String.valueOf(this.getPadreTutor()) + ", jugadorJoven=" + String.valueOf(this.getJugadorJoven()) + ", tipoRelacion=" + this.getTipoRelacion() + ", fechaAsociacion=" + String.valueOf(this.getFechaAsociacion()) + ", estado=" + this.getEstado() + ", permisosSupervision=" + this.getPermisosSupervision() + ", autorizado=" + this.getAutorizado() + ", codigoAcceso=" + this.getCodigoAcceso() + ")";
    }
}
