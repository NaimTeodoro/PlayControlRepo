import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RecursoEducativo } from '../../../models/recurso-educativo';
import { UsuarioRecursoEducativo } from '../../../models/usuario-recurso-educativo';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { infoTipoRecurso, RecursoService } from '../../../services/recurso-service';
import { isoLocal } from '../../../services/sesion-service';
import { UsuarioRecursoService } from '../../../services/usuario-recurso-service';

// Vista de solo lectura de un recurso educativo
@Component({
  selector: 'app-detalle-recurso',
  standalone: false,
  templateUrl: './detalle-recurso.html',
  styleUrl: './detalle-recurso.css',
})
export class DetalleRecurso {
  private notificacion = inject(NotificacionService);

  readonly recurso = signal<RecursoEducativo | null>(null);
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly infoTipo = infoTipoRecurso;
  private auth = inject(AuthService);
  private recursoService = inject(RecursoService);
  private consumoService = inject(UsuarioRecursoService);
  readonly esEditor = this.auth.tieneRol('ROLE_PROFESIONAL') || this.auth.tieneRol('ROLE_ADMIN');
  // /usuario-recursos solo lo usan jugador y padre (el profesional no tiene acceso)
  readonly registraConsumo = this.auth.tieneRol('ROLE_JUGADOR') || this.auth.tieneRol('ROLE_PADRE');

  readonly consumo = signal<UsuarioRecursoEducativo | null>(null);
  readonly guardando = signal(false);
  readonly estrellas = [1, 2, 3, 4, 5];

  constructor() {
    const id = Number(inject(ActivatedRoute).snapshot.paramMap.get('id'));
    this.recursoService
      .obtener(id)
      .subscribe({
        next: (r) => {
          this.recurso.set(r);
          this.cargando.set(false);
          if (this.registraConsumo) this.registrarAcceso(id);
        },
        error: (err) => {
          this.cargando.set(false);
          this.errorCarga.set(true);
          this.notificacion.error(err, 'No se encontró el recurso');
        },
      });
  }

  /** Al abrir: POST del consumo; si ya existe (409) se busca el registro y se actualiza con PUT */
  private registrarAcceso(idRecurso: number): void {
    const idUsuario = this.auth.getIdUsuario();
    if (idUsuario === null) return;
    const ahora = isoLocal(new Date());
    this.consumoService.crear({ idUsuario, idRecurso, fechaAcceso: ahora }).subscribe({
      next: (c) => this.consumo.set(c),
      error: (err) => {
        if (err?.status !== 409) return; // el consumo es secundario: no se molesta al usuario
        this.consumoService.porUsuario(idUsuario).subscribe({
          next: (lista) => {
            const previo = lista.find((x) => x.idRecurso === idRecurso);
            if (!previo) return;
            this.consumo.set(previo);
            this.consumoService
              .actualizar(previo.idUsuarioRecurso as number, { ...previo, fechaAcceso: ahora })
              .subscribe({ next: (c) => this.consumo.set(c), error: () => undefined });
          },
          error: () => undefined,
        });
      },
    });
  }

  /** PUT con los cambios del usuario (completado y/o valoración 1–5) */
  private guardar(cambios: Partial<UsuarioRecursoEducativo>, mensaje: string): void {
    const actual = this.consumo();
    if (!actual?.idUsuarioRecurso) return;
    this.guardando.set(true);
    this.consumoService
      .actualizar(actual.idUsuarioRecurso, { ...actual, ...cambios })
      .subscribe({
        next: (c) => {
          this.consumo.set(c);
          this.guardando.set(false);
          this.notificacion.exito(mensaje);
          // La valoración promedio del recurso la recalcula el servidor
          const idRecurso = this.recurso()?.idRecurso;
          if (idRecurso) {
            this.recursoService.obtener(idRecurso).subscribe({
              next: (r) => this.recurso.set(r),
              error: () => undefined,
            });
          }
        },
        error: (err) => {
          this.guardando.set(false);
          this.notificacion.error(err, 'No se pudo guardar');
        },
      });
  }

  marcarCompletado(): void {
    this.guardar({ completado: true }, '¡Recurso marcado como completado!');
  }

  valorar(estrellas: number): void {
    this.guardar({ valoracionUsuario: estrellas }, 'Gracias por tu valoración');
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
