package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Vinculo padre/tutor - jugador joven.
 * estado: PENDIENTE / ACTIVO / INACTIVO (PENDIENTE hasta que el hijo lo autoriza)
 * tipoRelacion: PADRE / MADRE / TUTOR
 */
@Entity
@Table(name = "padre_hijo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_padre_hijo", columnNames = {"id_padre_tutor", "id_jugador_joven"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PadreHijo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_padre_hijo")
    private Long idPadreHijo;

    @ManyToOne
    @JoinColumn(name = "id_padre_tutor", nullable = false)
    private PadreTutor padreTutor;

    @ManyToOne
    @JoinColumn(name = "id_jugador_joven", nullable = false)
    private JugadorJoven jugadorJoven;

    @Column(name = "tipo_relacion", length = 30)
    private String tipoRelacion;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "permisos_supervision", length = 500)
    private String permisosSupervision;

    @Column(name = "autorizado")
    private Boolean autorizado;
}
