import { Component, computed, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RecursoEducativo, TIPOS_RECURSO } from '../../../models/recurso-educativo';
import { Rol } from '../../../models/rol';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { infoTipoRecurso, RecursoService } from '../../../services/recurso-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Audiencias que ve cada rol de lectura (los editores ven todo)
const AUDIENCIAS: Partial<Record<Rol, string[]>> = {
  ROLE_JUGADOR: ['JUGADORES', 'TODOS'],
  ROLE_PADRE: ['PADRES', 'TODOS'],
};

// Recursos educativos. Jugador y padre solo leen (sin FAQ, que va en Soporte).
// Profesional y admin gestionan todo: crear, editar, eliminar, ver inactivos y FAQ.
@Component({
  selector: 'app-lista-recursos',
  standalone: false,
  templateUrl: './lista-recursos.html',
  styleUrl: './lista-recursos.css',
})
export class ListaRecursos {
  private auth = inject(AuthService);
  private recursoService = inject(RecursoService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly esEditor = this.auth.tieneRol('ROLE_PROFESIONAL') || this.auth.tieneRol('ROLE_ADMIN');

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  private recursos = signal<RecursoEducativo[]>([]);
  readonly busqueda = signal('');
  readonly tipo = signal('TODOS');

  readonly infoTipo = infoTipoRecurso;
  readonly tipos = [
    { valor: 'TODOS', etiqueta: 'Todos' },
    ...TIPOS_RECURSO.filter((t) => this.esEditor || t.valor !== 'FAQ'),
  ];

  // Lectores: activos, no FAQ y de su audiencia. Editores: todo.
  private visibles = computed(() => {
    if (this.esEditor) return this.recursos();
    const rol = this.auth.getRol();
    const audiencias = rol ? AUDIENCIAS[rol] : undefined;
    return this.recursos().filter(
      (r) =>
        r.activo !== false &&
        r.categoria !== 'FAQ' &&
        r.tipoRecurso !== 'FAQ' &&
        (!audiencias || !r.audienciaObjetivo || audiencias.includes(r.audienciaObjetivo)),
    );
  });

  readonly filtrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();
    const tipo = this.tipo();
    return this.visibles().filter(
      (r) =>
        (tipo === 'TODOS' || r.tipoRecurso === tipo) &&
        (!texto ||
          `${r.titulo} ${r.descripcion ?? ''} ${r.categoria ?? ''}`.toLowerCase().includes(texto)),
    );
  });

  constructor() {
    this.cargar();
  }

  private cargar(): void {
    this.recursoService.listar().subscribe({
      next: (lista) => {
        this.recursos.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  buscar(evento: Event): void {
    this.busqueda.set((evento.target as HTMLInputElement).value);
  }

  eliminar(r: RecursoEducativo): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar recurso?',
          mensaje: `Se eliminará "${r.titulo}" para todos los usuarios.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.recursoService.eliminar(r.idRecurso as number).subscribe({
          next: () => {
            this.notificacion.exito('Recurso eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
