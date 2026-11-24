package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Historial de una alerta (que paso despues de enviarla).
 * Usuario, tipo y respuesta se leen de la alerta: no se duplican aqui.
 * accionRealizada: TERMINO_SESION / PAUSA / IGNORADA
 * efectividadAlerta: ALTA / MEDIA / BAJA
 * estado: ABIERTO / CERRADO
 */
@Entity
@Table(name = "historial_alerta")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HistorialAlerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_alerta", nullable = false)
    private AlertaJuego alerta;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "accion_realizada", length = 500)
    private String accionRealizada;

    @Column(name = "impacto_en_sesion", length = 200)
    private String impactoEnSesion;

    @Column(name = "efectividad_alerta", length = 30)
    private String efectividadAlerta;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "notas_adicionales", columnDefinition = "TEXT")
    private String notasAdicionales;
}
