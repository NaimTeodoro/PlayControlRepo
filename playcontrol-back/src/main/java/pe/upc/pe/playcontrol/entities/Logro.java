package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Catalogo de logros.
 * tipo: DIARIO / SEMANAL / HITO
 * condicionTipo: DIAS_RESPETANDO_LIMITE / PAUSAS_REALIZADAS / DIAS_SIN_JUEGO / SESIONES_CORTAS / RECURSOS_COMPLETADOS
 * valorCondicion: cantidad necesaria de la condicion para obtener el logro.
 */
@Entity
@Table(name = "logro")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Logro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_logro")
    private Long idLogro;

    @Column(name = "nombre_logro", nullable = false, length = 200)
    private String nombreLogro;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "puntos_asignados", nullable = false)
    private Integer puntosAsignados;

    @Column(name = "tipo", length = 30)
    private String tipo;

    @Column(name = "condicion", columnDefinition = "TEXT")
    private String condicion;

    @Column(name = "condicion_tipo", length = 50)
    private String condicionTipo;

    @Column(name = "valor_condicion")
    private Integer valorCondicion;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "activo")
    private Boolean activo;
}
