package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Alerta enviada a un jugador.
 * tipoAlerta: LIMITE_DIARIO / AVISO_PREVIO / PAUSA / HORARIO_NOCTURNO / LIMITE_SEMANAL
 * intensidad: BAJA / MEDIA / ALTA
 * estadoAlerta: ENVIADA / LEIDA / RESPONDIDA
 * tiempoTranscurrido y tiempoRespuesta en minutos; limiteAlcanzado en horas.
 */
@Entity
@Table(name = "alerta_juego")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AlertaJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_sesion")
    private SesionJuego sesion;

    @Column(name = "tipo_alerta", length = 50)
    private String tipoAlerta;

    @Column(name = "mensaje", length = 500)
    private String mensaje;

    @Column(name = "intensidad", length = 20)
    private String intensidad;

    @Column(name = "tiempo_transcurrido")
    private Integer tiempoTranscurrido;

    @Column(name = "limite_alcanzado", precision = 4, scale = 2)
    private BigDecimal limiteAlcanzado;

    @Column(name = "accion_sugerida", length = 500)
    private String accionSugerida;

    @Column(name = "respondido_usuario")
    private Boolean respondidoUsuario;

    @Column(name = "respuesta_usuario", length = 500)
    private String respuestaUsuario;

    @Column(name = "tiempo_respuesta")
    private Integer tiempoRespuesta;

    @Column(name = "estado_alerta", length = 20)
    private String estadoAlerta;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}
