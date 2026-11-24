package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Perfil de jugador joven (1 a 1 con usuario).
 * codigoVinculacion: PC-XXXXXX, se genera al crear el perfil; el padre lo ingresa para vincularse.
 * diasJuegoPreferidos: LUN,MAR,MIE,JUE,VIE,SAB,DOM
 */
@Entity
@Table(name = "jugador_joven", uniqueConstraints = {
        @UniqueConstraint(name = "uk_jugador_joven_usuario", columnNames = "id_usuario"),
        @UniqueConstraint(name = "uk_jugador_joven_codigo", columnNames = "codigo_vinculacion")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class JugadorJoven {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador_joven")
    private Long idJugadorJoven;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "codigo_vinculacion", nullable = false, length = 20)
    private String codigoVinculacion;

    @Column(name = "nivel_habilidad", length = 50)
    private String nivelHabilidad;

    @Column(name = "genero_preferido", length = 100)
    private String generoPreferido;

    @Column(name = "hora_preferida_inicio")
    private LocalTime horaPreferidaInicio;

    @Column(name = "hora_preferida_fin")
    private LocalTime horaPreferidaFin;

    @Column(name = "dias_juego_preferidos", length = 100)
    private String diasJuegoPreferidos;

    @Column(name = "motivacion_principal", length = 50)
    private String motivacionPrincipal;

    @Column(name = "problemas_identificados", columnDefinition = "TEXT")
    private String problemasIdentificados;

    @Column(name = "historial_problemas", columnDefinition = "TEXT")
    private String historialProblemas;

    @Column(name = "fecha_registro_segmento")
    private LocalDateTime fechaRegistroSegmento;
}
