package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Dispositivo asociado a un usuario.
 * tipoDispositivo: PC / MOVIL / CONSOLA
 * estado: ACTIVO / INACTIVO
 */
@Entity
@Table(name = "dispositivo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo")
    private Long idDispositivo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "tipo_dispositivo", length = 20)
    private String tipoDispositivo;

    @Column(name = "modelo", length = 150)
    private String modelo;

    @Column(name = "sistema_operativo", length = 100)
    private String sistemaOperativo;

    @Column(name = "identificador_disp", length = 100)
    private String identificadorDisp;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(name = "version_app", length = 20)
    private String versionApp;
}
