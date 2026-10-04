package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(
        name = "alerta_juego"
)
public class AlertaJuego {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_alerta"
    )
    private Long idAlerta;
    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            nullable = false
    )
    private User user;
    @ManyToOne
    @JoinColumn(
            name = "id_sesion"
    )
    private SessionJuego sesion;
    @Column(
            name = "tipo_alerta",
            length = 50
    )
    private String tipoAlerta;
    @Column(
            name = "mensaje",
            length = 500
    )
    private String mensaje;
    @Column(
            name = "intensidad",
            length = 20
    )
    private String intensidad;
    @Column(
            name = "tiempo_transcurrido"
    )
    private Integer tiempoTranscurrido;
    @Column(
            name = "limite_alcanzado",
            precision = 4,
            scale = 2
    )
    private BigDecimal limiteAlcanzado;
    @Column(
            name = "accion_sugerida",
            length = 500
    )
    private String accionSugerida;
    @Column(
            name = "respondido_usuario"
    )
    private Boolean respondidoUsuario;
    @Column(
            name = "respuesta_usuario",
            length = 500
    )
    private String respuestaUsuario;
    @Column(
            name = "tiempo_respuesta"
    )
    private Integer tiempoRespuesta;
    @Column(
            name = "estado_alerta",
            length = 20
    )
    private String estadoAlerta;
    @Column(
            name = "fecha_envio"
    )
    private LocalDateTime fechaEnvio;

    @Generated
    public AlertaJuego() {
    }

    @Generated
    public Long getIdAlerta() {
        return this.idAlerta;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public SessionJuego getSesion() {
        return this.sesion;
    }

    @Generated
    public String getTipoAlerta() {
        return this.tipoAlerta;
    }

    @Generated
    public String getMensaje() {
        return this.mensaje;
    }

    @Generated
    public String getIntensidad() {
        return this.intensidad;
    }

    @Generated
    public Integer getTiempoTranscurrido() {
        return this.tiempoTranscurrido;
    }

    @Generated
    public BigDecimal getLimiteAlcanzado() {
        return this.limiteAlcanzado;
    }

    @Generated
    public String getAccionSugerida() {
        return this.accionSugerida;
    }

    @Generated
    public Boolean getRespondidoUsuario() {
        return this.respondidoUsuario;
    }

    @Generated
    public String getRespuestaUsuario() {
        return this.respuestaUsuario;
    }

    @Generated
    public Integer getTiempoRespuesta() {
        return this.tiempoRespuesta;
    }

    @Generated
    public String getEstadoAlerta() {
        return this.estadoAlerta;
    }

    @Generated
    public LocalDateTime getFechaEnvio() {
        return this.fechaEnvio;
    }

    @Generated
    public void setIdAlerta(final Long idAlerta) {
        this.idAlerta = idAlerta;
    }

    @Generated
    public void setUser(final User user) {
        this.user = user;
    }

    @Generated
    public void setSesion(final SessionJuego sesion) {
        this.sesion = sesion;
    }

    @Generated
    public void setTipoAlerta(final String tipoAlerta) {
        this.tipoAlerta = tipoAlerta;
    }

    @Generated
    public void setMensaje(final String mensaje) {
        this.mensaje = mensaje;
    }

    @Generated
    public void setIntensidad(final String intensidad) {
        this.intensidad = intensidad;
    }

    @Generated
    public void setTiempoTranscurrido(final Integer tiempoTranscurrido) {
        this.tiempoTranscurrido = tiempoTranscurrido;
    }

    @Generated
    public void setLimiteAlcanzado(final BigDecimal limiteAlcanzado) {
        this.limiteAlcanzado = limiteAlcanzado;
    }

    @Generated
    public void setAccionSugerida(final String accionSugerida) {
        this.accionSugerida = accionSugerida;
    }

    @Generated
    public void setRespondidoUsuario(final Boolean respondidoUsuario) {
        this.respondidoUsuario = respondidoUsuario;
    }

    @Generated
    public void setRespuestaUsuario(final String respuestaUsuario) {
        this.respuestaUsuario = respuestaUsuario;
    }

    @Generated
    public void setTiempoRespuesta(final Integer tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    @Generated
    public void setEstadoAlerta(final String estadoAlerta) {
        this.estadoAlerta = estadoAlerta;
    }

    @Generated
    public void setFechaEnvio(final LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AlertaJuego)) {
            return false;
        } else {
            AlertaJuego other = (AlertaJuego)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$idAlerta = this.getIdAlerta();
                Object other$idAlerta = other.getIdAlerta();
                if (this$idAlerta == null) {
                    if (other$idAlerta != null) {
                        return false;
                    }
                } else if (!this$idAlerta.equals(other$idAlerta)) {
                    return false;
                }

                Object this$tiempoTranscurrido = this.getTiempoTranscurrido();
                Object other$tiempoTranscurrido = other.getTiempoTranscurrido();
                if (this$tiempoTranscurrido == null) {
                    if (other$tiempoTranscurrido != null) {
                        return false;
                    }
                } else if (!this$tiempoTranscurrido.equals(other$tiempoTranscurrido)) {
                    return false;
                }

                Object this$respondidoUsuario = this.getRespondidoUsuario();
                Object other$respondidoUsuario = other.getRespondidoUsuario();
                if (this$respondidoUsuario == null) {
                    if (other$respondidoUsuario != null) {
                        return false;
                    }
                } else if (!this$respondidoUsuario.equals(other$respondidoUsuario)) {
                    return false;
                }

                Object this$tiempoRespuesta = this.getTiempoRespuesta();
                Object other$tiempoRespuesta = other.getTiempoRespuesta();
                if (this$tiempoRespuesta == null) {
                    if (other$tiempoRespuesta != null) {
                        return false;
                    }
                } else if (!this$tiempoRespuesta.equals(other$tiempoRespuesta)) {
                    return false;
                }

                Object this$user = this.getUser();
                Object other$user = other.getUser();
                if (this$user == null) {
                    if (other$user != null) {
                        return false;
                    }
                } else if (!this$user.equals(other$user)) {
                    return false;
                }

                Object this$sesion = this.getSesion();
                Object other$sesion = other.getSesion();
                if (this$sesion == null) {
                    if (other$sesion != null) {
                        return false;
                    }
                } else if (!this$sesion.equals(other$sesion)) {
                    return false;
                }

                Object this$tipoAlerta = this.getTipoAlerta();
                Object other$tipoAlerta = other.getTipoAlerta();
                if (this$tipoAlerta == null) {
                    if (other$tipoAlerta != null) {
                        return false;
                    }
                } else if (!this$tipoAlerta.equals(other$tipoAlerta)) {
                    return false;
                }

                Object this$mensaje = this.getMensaje();
                Object other$mensaje = other.getMensaje();
                if (this$mensaje == null) {
                    if (other$mensaje != null) {
                        return false;
                    }
                } else if (!this$mensaje.equals(other$mensaje)) {
                    return false;
                }

                Object this$intensidad = this.getIntensidad();
                Object other$intensidad = other.getIntensidad();
                if (this$intensidad == null) {
                    if (other$intensidad != null) {
                        return false;
                    }
                } else if (!this$intensidad.equals(other$intensidad)) {
                    return false;
                }

                Object this$limiteAlcanzado = this.getLimiteAlcanzado();
                Object other$limiteAlcanzado = other.getLimiteAlcanzado();
                if (this$limiteAlcanzado == null) {
                    if (other$limiteAlcanzado != null) {
                        return false;
                    }
                } else if (!this$limiteAlcanzado.equals(other$limiteAlcanzado)) {
                    return false;
                }

                Object this$accionSugerida = this.getAccionSugerida();
                Object other$accionSugerida = other.getAccionSugerida();
                if (this$accionSugerida == null) {
                    if (other$accionSugerida != null) {
                        return false;
                    }
                } else if (!this$accionSugerida.equals(other$accionSugerida)) {
                    return false;
                }

                Object this$respuestaUsuario = this.getRespuestaUsuario();
                Object other$respuestaUsuario = other.getRespuestaUsuario();
                if (this$respuestaUsuario == null) {
                    if (other$respuestaUsuario != null) {
                        return false;
                    }
                } else if (!this$respuestaUsuario.equals(other$respuestaUsuario)) {
                    return false;
                }

                Object this$estadoAlerta = this.getEstadoAlerta();
                Object other$estadoAlerta = other.getEstadoAlerta();
                if (this$estadoAlerta == null) {
                    if (other$estadoAlerta != null) {
                        return false;
                    }
                } else if (!this$estadoAlerta.equals(other$estadoAlerta)) {
                    return false;
                }

                Object this$fechaEnvio = this.getFechaEnvio();
                Object other$fechaEnvio = other.getFechaEnvio();
                if (this$fechaEnvio == null) {
                    if (other$fechaEnvio != null) {
                        return false;
                    }
                } else if (!this$fechaEnvio.equals(other$fechaEnvio)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof AlertaJuego;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idAlerta = this.getIdAlerta();
        result = result * 59 + ($idAlerta == null ? 43 : $idAlerta.hashCode());
        Object $tiempoTranscurrido = this.getTiempoTranscurrido();
        result = result * 59 + ($tiempoTranscurrido == null ? 43 : $tiempoTranscurrido.hashCode());
        Object $respondidoUsuario = this.getRespondidoUsuario();
        result = result * 59 + ($respondidoUsuario == null ? 43 : $respondidoUsuario.hashCode());
        Object $tiempoRespuesta = this.getTiempoRespuesta();
        result = result * 59 + ($tiempoRespuesta == null ? 43 : $tiempoRespuesta.hashCode());
        Object $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        Object $sesion = this.getSesion();
        result = result * 59 + ($sesion == null ? 43 : $sesion.hashCode());
        Object $tipoAlerta = this.getTipoAlerta();
        result = result * 59 + ($tipoAlerta == null ? 43 : $tipoAlerta.hashCode());
        Object $mensaje = this.getMensaje();
        result = result * 59 + ($mensaje == null ? 43 : $mensaje.hashCode());
        Object $intensidad = this.getIntensidad();
        result = result * 59 + ($intensidad == null ? 43 : $intensidad.hashCode());
        Object $limiteAlcanzado = this.getLimiteAlcanzado();
        result = result * 59 + ($limiteAlcanzado == null ? 43 : $limiteAlcanzado.hashCode());
        Object $accionSugerida = this.getAccionSugerida();
        result = result * 59 + ($accionSugerida == null ? 43 : $accionSugerida.hashCode());
        Object $respuestaUsuario = this.getRespuestaUsuario();
        result = result * 59 + ($respuestaUsuario == null ? 43 : $respuestaUsuario.hashCode());
        Object $estadoAlerta = this.getEstadoAlerta();
        result = result * 59 + ($estadoAlerta == null ? 43 : $estadoAlerta.hashCode());
        Object $fechaEnvio = this.getFechaEnvio();
        result = result * 59 + ($fechaEnvio == null ? 43 : $fechaEnvio.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getIdAlerta();
        return "AlertaJuego(idAlerta=" + var10000 + ", user=" + String.valueOf(this.getUser()) + ", sesion=" + String.valueOf(this.getSesion()) + ", tipoAlerta=" + this.getTipoAlerta() + ", mensaje=" + this.getMensaje() + ", intensidad=" + this.getIntensidad() + ", tiempoTranscurrido=" + this.getTiempoTranscurrido() + ", limiteAlcanzado=" + String.valueOf(this.getLimiteAlcanzado()) + ", accionSugerida=" + this.getAccionSugerida() + ", respondidoUsuario=" + this.getRespondidoUsuario() + ", respuestaUsuario=" + this.getRespuestaUsuario() + ", tiempoRespuesta=" + this.getTiempoRespuesta() + ", estadoAlerta=" + this.getEstadoAlerta() + ", fechaEnvio=" + String.valueOf(this.getFechaEnvio()) + ")";
    }
}
