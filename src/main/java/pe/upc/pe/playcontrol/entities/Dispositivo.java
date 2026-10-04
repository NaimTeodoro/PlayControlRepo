package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispositivos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDispositivo;

    @Column(name = "tipo_dispositivo", length = 20)
    private String tipoDispositivo;

    @Column(name = "modelo", length = 150)
    private String modelo;

    @Column(name = "sistema_operativo", length = 100)
    private String sistemaOperativo;

    @Column(name = "identificador_disp", length = 500)
    private String identificadorDisp;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "ultima_actividad")
    private LocalDateTime ultimaActividad;

    @Column(name = "version_app", length = 20)
    private String versionApp;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;
}