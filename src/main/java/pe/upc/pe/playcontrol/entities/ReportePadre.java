package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reporte periodico para un padre sobre un hijo vinculado (ligado a padre_hijo).
 * periodoReporte: SEMANAL / MENSUAL
 * tendencia: MEJORA / ESTABLE / EMPEORA (comparado con el periodo anterior)
 * respetoLimitesPorc: 0.00 a 100.00
 */
@Entity
@Table(name = "reporte_padre")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReportePadre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_padre_hijo", nullable = false)
    private PadreHijo padreHijo;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "periodo_reporte", length = 20)
    private String periodoReporte;

    @Column(name = "fecha_inicio_periodo")
    private LocalDate fechaInicioPeriodo;

    @Column(name = "fecha_fin_periodo")
    private LocalDate fechaFinPeriodo;

    @Column(name = "tiempo_total_juego_minutos")
    private Integer tiempoTotalJuegoMinutos;

    @Column(name = "tiempo_promedio_sesion_minutos")
    private Integer tiempoPromedioSesionMinutos;

    @Column(name = "sesiones_juego_total")
    private Integer sesionesJuegoTotal;

    @Column(name = "videojuegos_jugados")
    private Integer videojuegosJugados;

    @Column(name = "dias_activos")
    private Integer diasActivos;

    @Column(name = "dias_sin_juego")
    private Integer diasSinJuego;

    @Column(name = "respeto_limites_porc", precision = 5, scale = 2)
    private BigDecimal respetoLimitesPorc;

    @Column(name = "sesiones_respetuosas")
    private Integer sesionesRespetuosas;

    @Column(name = "alertas_recibidas")
    private Integer alertasRecibidas;

    @Column(name = "alertas_respondidas")
    private Integer alertasRespondidas;

    @Column(name = "logros_alcanzados")
    private Integer logrosAlcanzados;

    @Column(name = "puntos_obtenidos")
    private Integer puntosObtenidos;

    @Column(name = "racha_dias")
    private Integer rachaDias;

    @Column(name = "recomendaciones_sistema", columnDefinition = "TEXT")
    private String recomendacionesSistema;

    @Column(name = "alertas_importantes", columnDefinition = "TEXT")
    private String alertasImportantes;

    @Column(name = "tendencia", length = 20)
    private String tendencia;

    @Column(name = "enviado")
    private Boolean enviado;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "visto")
    private Boolean visto;

    @Column(name = "fecha_visto")
    private LocalDateTime fechaVisto;
}
