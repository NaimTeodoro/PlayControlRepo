package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Limite de juego de un jugador.
 * usuario: jugador al que aplica. creadoPor: jugador o padre que lo configuro.
 * diasPermitidos: lista separada por comas sin tildes: LUN,MAR,MIE,JUE,VIE,SAB,DOM
 * metodoBloqueo: NOTIFICACION / BLOQUEO_SIMPLE / BLOQUEO_TOTAL
 * accionAlExceder: ALERTAR / BLOQUEAR
 * estado: ACTIVO / INACTIVO (un solo ACTIVO por usuario)
 */
@Entity
@Table(name = "limite_juego")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LimiteJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_limite")
    private Long idLimite;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_creado_por")
    private User creadoPor;

    @Column(name = "limite_horas_diarias", precision = 4, scale = 2)
    private BigDecimal limiteHorasDiarias;

    @Column(name = "limite_horas_semanal", precision = 6, scale = 2)
    private BigDecimal limiteHorasSemanal;

    @Column(name = "limite_horas_mensual", precision = 8, scale = 2)
    private BigDecimal limiteHorasMensual;

    @Column(name = "hora_inicio_dia")
    private LocalTime horaInicioDia;

    @Column(name = "hora_fin_dia")
    private LocalTime horaFinDia;

    @Column(name = "dias_permitidos", length = 100)
    private String diasPermitidos;

    @Column(name = "minutos_aviso_previo")
    private Integer minutosAvisoPrevio;

    @Column(name = "metodo_bloqueo", length = 30)
    private String metodoBloqueo;

    @Column(name = "accion_al_exceder", length = 30)
    private String accionAlExceder;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_modificacion")
    private LocalDateTime ultimaModificacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
