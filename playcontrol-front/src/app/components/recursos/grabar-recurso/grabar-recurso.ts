import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  Audiencia,
  AUDIENCIAS,
  NivelDificultad,
  NIVELES_DIFICULTAD,
  RecursoEducativo,
  TipoRecurso,
  TIPOS_RECURSO,
} from '../../../models/recurso-educativo';
import { NotificacionService } from '../../../services/notificacion-service';
import { RecursoService } from '../../../services/recurso-service';

// Formulario único para crear y editar un recurso educativo (editar si la ruta trae :id).
// Acceso: PROFESIONAL / ADMIN.
@Component({
  selector: 'app-grabar-recurso',
  standalone: false,
  templateUrl: './grabar-recurso.html',
  styleUrl: './grabar-recurso.css',
})
export class GrabarRecurso {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recursoService = inject(RecursoService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);

  readonly tipos = TIPOS_RECURSO;
  readonly audiencias = AUDIENCIAS;
  readonly niveles = NIVELES_DIFICULTAD;

  readonly form = this.fb.nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(300)]],
    tipoRecurso: ['ARTICULO' as TipoRecurso, Validators.required],
    audienciaObjetivo: ['TODOS' as Audiencia, Validators.required],
    categoria: ['', Validators.maxLength(100)],
    subcategoria: ['', Validators.maxLength(100)],
    nivelDificultad: ['BASICO' as NivelDificultad],
    formato: ['', Validators.maxLength(30)],
    duracionMinutos: [null as number | null, Validators.min(0)],
    autor: ['', Validators.maxLength(200)],
    urlRecurso: ['', Validators.maxLength(500)],
    urlImagen: ['', Validators.maxLength(500)],
    descripcion: [''],
    contenido: [''],
    activo: [true],
  });

  constructor() {
    if (this.id !== null) {
      this.recursoService.obtener(this.id).subscribe({
        next: (r) => {
          this.form.patchValue({
            titulo: r.titulo,
            tipoRecurso: r.tipoRecurso ?? 'ARTICULO',
            audienciaObjetivo: r.audienciaObjetivo ?? 'TODOS',
            categoria: r.categoria ?? '',
            subcategoria: r.subcategoria ?? '',
            nivelDificultad: r.nivelDificultad ?? 'BASICO',
            formato: r.formato ?? '',
            duracionMinutos: r.duracionMinutos ?? null,
            autor: r.autor ?? '',
            urlRecurso: r.urlRecurso ?? '',
            urlImagen: r.urlImagen ?? '',
            descripcion: r.descripcion ?? '',
            contenido: r.contenido ?? '',
            activo: r.activo ?? true,
          });
          this.cargando.set(false);
        },
        error: (err) => {
          this.cargando.set(false);
          this.notificacion.error(err, 'No se encontró el recurso');
          this.volver();
        },
      });
    }
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // El request no lleva id, fechas, visitas ni valoración (los maneja el backend)
    const recurso: RecursoEducativo = {
      titulo: v.titulo.trim(),
      tipoRecurso: v.tipoRecurso,
      audienciaObjetivo: v.audienciaObjetivo,
      categoria: v.tipoRecurso === 'FAQ' && !v.categoria.trim() ? 'FAQ' : v.categoria,
      subcategoria: v.subcategoria,
      nivelDificultad: v.nivelDificultad,
      formato: v.formato,
      duracionMinutos: v.duracionMinutos ?? undefined,
      autor: v.autor,
      urlRecurso: v.urlRecurso,
      urlImagen: v.urlImagen,
      descripcion: v.descripcion,
      contenido: v.contenido,
      activo: v.activo,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.recursoService.actualizar(this.id, recurso)
        : this.recursoService.crear(recurso);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Recurso actualizado' : 'Recurso creado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el recurso', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/recursos']);
  }
}
