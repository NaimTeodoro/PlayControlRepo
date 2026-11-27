import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Plataforma, TipoPlataforma, TIPOS_PLATAFORMA } from '../../../models/plataforma';
import { NotificacionService } from '../../../services/notificacion-service';
import { PlataformaService } from '../../../services/plataforma-service';
import { isoLocal } from '../../../services/sesion-service';

// Formulario único para crear y editar una plataforma (editar si la ruta trae :id)
@Component({
  selector: 'app-grabar-plataforma',
  standalone: false,
  templateUrl: './grabar-plataforma.html',
  styleUrl: './grabar-plataforma.css',
})
export class GrabarPlataforma {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private plataformaService = inject(PlataformaService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);
  readonly tipos = TIPOS_PLATAFORMA;

  private original: Plataforma | null = null;

  readonly form = this.fb.nonNullable.group({
    nombrePlataforma: ['', [Validators.required, Validators.maxLength(100)]],
    tipo: ['PC' as TipoPlataforma, Validators.required],
    sistemaOperativo: ['', Validators.maxLength(100)],
    desarrollador: ['', Validators.maxLength(150)],
    descripcion: [''],
    activa: [true],
  });

  constructor() {
    if (this.id !== null) {
      this.plataformaService.obtener(this.id).subscribe({
        next: (p) => {
          this.original = p;
          this.form.patchValue({
            nombrePlataforma: p.nombrePlataforma,
            tipo: p.tipo ?? 'PC',
            sistemaOperativo: p.sistemaOperativo ?? '',
            desarrollador: p.desarrollador ?? '',
            descripcion: p.descripcion ?? '',
            activa: p.activa ?? true,
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
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se fusiona con el original porque PUT reemplaza el registro completo
    const plataforma: Plataforma = {
      ...this.original,
      nombrePlataforma: v.nombrePlataforma.trim(),
      tipo: v.tipo,
      sistemaOperativo: v.sistemaOperativo,
      desarrollador: v.desarrollador,
      descripcion: v.descripcion,
      activa: v.activa,
      fechaCreacion: this.original?.fechaCreacion ?? isoLocal(),
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.plataformaService.actualizar(this.id, plataforma)
        : this.plataformaService.crear(plataforma);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Plataforma actualizada' : 'Plataforma creada');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar la plataforma', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/plataformas']);
  }
}
