package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Perfil de profesional de salud (1 a 1 con usuario).
 * estado: ACTIVO / INACTIVO / PENDIENTE
 */
@Entity
@Table(name = "profesional_salud", uniqueConstraints = {
        @UniqueConstraint(name = "uk_profesional_salud_usuario", columnNames = "id_usuario")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProfesionalSalud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional")
    private Long idProfesional;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "especialidad", length = 150, nullable = false)
    private String especialidad;

    @Column(name = "numero_licencia", length = 100, nullable = false)
    private String numeroLicencia;

    @Column(name = "institucion", length = 200)
    private String institucion;

    @Column(name = "anios_experiencia")
    private Integer aniosExperiencia;

    @Column(name = "area_trabajo", length = 50)
    private String areaTrabajo;

    @Column(name = "certificaciones", columnDefinition = "TEXT")
    private String certificaciones;

    @Column(name = "telefono_consulta", length = 20)
    private String telefonoConsulta;

    @Column(name = "horario_consulta", length = 200)
    private String horarioConsulta;

    @Column(name = "verificado_por", length = 100)
    private String verificadoPor;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;
}
