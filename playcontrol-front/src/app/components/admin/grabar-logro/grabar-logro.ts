import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CONDICIONES_LOGRO, Logro, TIPOS_LOGRO } from '../../../models/logro';
import { LogroService } from '../../../services/logro-service';
import { NotificacionService } from '../../../services/notificacion-service';

// Formulario único para crear y editar un logro del catálogo (editar si la ruta trae :id)
@Component({
  selector: 'app-grabar-logro',
  standalone: false,
  templateUrl: './grabar-logro.html',
  styleUrl: './grabar-logro.css',
})
export class GrabarLogro {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private logroService = inject(LogroService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);
  readonly tipos = TIPOS_LOGRO;
  readonly condiciones = CONDICIONES_LOGRO;

  private original: Logro | null = null;

  readonly form = this.fb.nonNullable.group({
    nombreLogro: ['', [Validators.required, Validators.maxLength(150)]],
    descripcion: [''],
    puntosAsignados: [50, [Validators.required, Validators.min(0)]],
    tipo: ['HITO' as string],
    condicionTipo: [null as string | null],
    valorCondicion: [null as number | null, Validators.min(1)],
    condicion: [''], // texto libre que se muestra al jugador
    imagenUrl: ['', Validators.maxLength(500)],
    activo: [true],
  });

  constructor() {
    if (this.id === null) return;
    this.logroService.obtener(this.id).subscribe({
      next: (l) => {
        this.original = l;
        this.form.patchValue({
          nombreLogro: l.nombreLogro,
          descripcion: l.descripcion ?? '',
          puntosAsignados: l.puntosAsignados,
          tipo: l.tipo ?? 'HITO',
          condicionTipo: l.condicionTipo ?? null,
          valorCondicion: l.valorCondicion ?? null,
          condicion: l.condicion ?? '',
          imagenUrl: l.imagenUrl ?? '',
          activo: l.activo ?? true,
        });
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.notificacion.error(err);
        this.volver();
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se fusiona con el original porque PUT reemplaza el registro completo
    const logro: Logro = {
      ...this.original,
      nombreLogro: v.nombreLogro.trim(),
      descripcion: v.descripcion,
      puntosAsignados: v.puntosAsignados,
      tipo: v.tipo as Logro['tipo'],
      condicionTipo: v.condicionTipo as Logro['condicionTipo'],
      valorCondicion: v.valorCondicion,
      condicion: v.condicion,
      imagenUrl: v.imagenUrl.trim() || null,
      activo: v.activo,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.logroService.actualizar(this.id, logro)
        : this.logroService.crear(logro);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Logro actualizado' : 'Logro creado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el logro', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/logros-catalogo']);
  }
}
