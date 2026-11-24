package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Relacion profesional de salud - jugador joven (paciente).
 * estado: ACTIVO / INACTIVO / PENDIENTE
 */
@Entity
@Table(name = "profesional_cliente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_profesional_cliente", columnNames = {"id_profesional", "id_jugador_joven"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProfesionalCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional_cliente")
    private Long idProfesionalCliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_profesional", nullable = false)
    private ProfesionalSalud profesional;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_jugador_joven", nullable = false)
    private JugadorJoven jugadorJoven;

    @Column(name = "fecha_asociacion", nullable = false)
    private LocalDateTime fechaAsociacion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "plan_tratamiento", columnDefinition = "TEXT")
    private String planTratamiento;

    @Column(name = "frecuencia_consulta", length = 100)
    private String frecuenciaConsulta;

    @Column(name = "proxima_consulta")
    private LocalDateTime proximaConsulta;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
}
