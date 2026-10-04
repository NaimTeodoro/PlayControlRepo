package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Generated;

@Entity
@Table(
        name = "jugador_joven"
)
public class JugadorJoven {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "id_jugador_joven"
    )
    private Long idJugadorJoven;
    @OneToOne
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            unique = true
    )
    private User user;
    @Column(
            name = "nivel_habilidad",
            length = 50
    )
    private String nivelHabilidad;
    @Column(
            name = "genero_preferido",
            length = 100
    )
    private String generoPreferido;
    @Column(
            name = "hora_preferida_inicio"
    )
    private LocalTime horaPreferidaInicio;
    @Column(
            name = "hora_preferida_fin"
    )
    private LocalTime horaPreferidaFin;
    @Column(
            name = "dias_juego_preferidos",
            length = 100
    )
    private String diasJuegoPreferidos;
    @Column(
            name = "motivacion_principal",
            length = 50
    )
    private String motivacionPrincipal;
    @Column(
            name = "problemas_identificados",
            columnDefinition = "TEXT"
    )
    private String problemasIdentificados;
    @Column(
            name = "historial_problemas",
            columnDefinition = "TEXT"
    )
    private String historialProblemas;
    @Column(
            name = "fecha_registro_segmento"
    )
    private LocalDateTime fechaRegistroSegmento;

    @Generated
    public JugadorJoven() {
    }

    @Generated
    public Long getIdJugadorJoven() {
        return this.idJugadorJoven;
    }

    @Generated
    public User getUser() {
        return this.user;
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
    public void setUser(final User user) {
        this.user = user;
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
        } else if (!(o instanceof JugadorJoven)) {
            return false;
        } else {
            JugadorJoven other = (JugadorJoven)o;
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

                Object this$user = this.getUser();
                Object other$user = other.getUser();
                if (this$user == null) {
                    if (other$user != null) {
                        return false;
                    }
                } else if (!this$user.equals(other$user)) {
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
        return other instanceof JugadorJoven;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $idJugadorJoven = this.getIdJugadorJoven();
        result = result * 59 + ($idJugadorJoven == null ? 43 : $idJugadorJoven.hashCode());
        Object $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
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
        return "JugadorJoven(idJugadorJoven=" + var10000 + ", user=" + String.valueOf(this.getUser()) + ", nivelHabilidad=" + this.getNivelHabilidad() + ", generoPreferido=" + this.getGeneroPreferido() + ", horaPreferidaInicio=" + String.valueOf(this.getHoraPreferidaInicio()) + ", horaPreferidaFin=" + String.valueOf(this.getHoraPreferidaFin()) + ", diasJuegoPreferidos=" + this.getDiasJuegoPreferidos() + ", motivacionPrincipal=" + this.getMotivacionPrincipal() + ", problemasIdentificados=" + this.getProblemasIdentificados() + ", historialProblemas=" + this.getHistorialProblemas() + ", fechaRegistroSegmento=" + String.valueOf(this.getFechaRegistroSegmento()) + ")";
    }
}
