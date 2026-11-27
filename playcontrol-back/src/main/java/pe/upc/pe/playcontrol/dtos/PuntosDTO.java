package pe.upc.pe.playcontrol.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de GET /api/recompensas/usuario/{idUsuario}/puntos.
 * puntosTotales = suma de puntosAsignados de los logros OBTENIDA + RECLAMADA.
 * logrosObtenidos = cantidad OBTENIDA + RECLAMADA. logrosEnProgreso = cantidad EN_PROGRESO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntosDTO {
    private Integer puntosTotales;
    private Integer logrosObtenidos;
    private Integer logrosEnProgreso;
}
