package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "plataformas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Plataforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlataforma;

    @Column(name = "nombre_plataforma", nullable = false, length = 100)
    private String nombrePlataforma;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "desarrollador", length = 150)
    private String desarrollador;

    @Column(name = "sistema_operativo", length = 100)
    private String sistemaOperativo;

    @Column(name = "activa")
    private Boolean activa;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
