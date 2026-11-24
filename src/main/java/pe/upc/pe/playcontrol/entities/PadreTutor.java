package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Perfil de padre o tutor (1 a 1 con usuario).
 * El numero de hijos y los hijos monitoreados se obtienen de padre_hijo; el nivel de tecnologia, de usuario.
 * frecuenciaRevisionReportes: DIARIA / SEMANAL / MENSUAL
 */
@Entity
@Table(name = "padre_tutor", uniqueConstraints = {
        @UniqueConstraint(name = "uk_padre_tutor_usuario", columnNames = "id_usuario")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PadreTutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_padre_tutor")
    private Long idPadreTutor;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "metodos_control_actual", columnDefinition = "TEXT")
    private String metodosControlActual;

    @Column(name = "preocupaciones_principales", columnDefinition = "TEXT")
    private String preocupacionesPrincipales;

    @Column(name = "objetivos_supervision", columnDefinition = "TEXT")
    private String objetivosSupervision;

    @Column(name = "frecuencia_revision_reportes", length = 20)
    private String frecuenciaRevisionReportes;

    @Column(name = "configuracion_alertas", columnDefinition = "TEXT")
    private String configuracionAlertas;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
