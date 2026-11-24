package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reporte para un padre sobre un hijo vinculado. idPadreTutor, idJugadorJoven y nombreHijo son de solo lectura.
 * periodoReporte: SEMANAL / MENSUAL. tendencia: MEJORA / ESTABLE / EMPEORA
 */
@Data
public class ReportePadreDTO {
    private Long idReporte;
    @NotNull
    private Long idPadreHijo;
    private LocalDateTime fechaGeneracion;
    @Pattern(regexp = "SEMANAL|MENSUAL")
    private String periodoReporte;
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;
    private Integer tiempoTotalJuegoMinutos;
    private Integer tiempoPromedioSesionMinutos;
    private Integer sesionesJuegoTotal;
    private Integer videojuegosJugados;
    private Integer diasActivos;
    private Integer diasSinJuego;
    private BigDecimal respetoLimitesPorc;
    private Integer sesionesRespetuosas;
    private Integer alertasRecibidas;
    private Integer alertasRespondidas;
    private Integer logrosAlcanzados;
    private Integer puntosObtenidos;
    private Integer rachaDias;
    private String recomendacionesSistema;
    private String alertasImportantes;
    @Pattern(regexp = "MEJORA|ESTABLE|EMPEORA")
    private String tendencia;
    private Boolean enviado;
    private LocalDateTime fechaEnvio;
    private Boolean visto;
    private LocalDateTime fechaVisto;

    // Solo lectura
    private Long idPadreTutor;
    private Long idJugadorJoven;
    private String nombreHijo;
}
