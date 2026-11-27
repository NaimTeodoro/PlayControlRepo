import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { Videojuego } from '../../../models/videojuego';
import { Plataforma } from '../../../models/plataforma';
import { VideojuegoService } from '../../../services/videojuego-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { PlataformaService } from '../../../services/plataforma-service';

/** Date → yyyy-MM-dd (sin zona) */
function aFechaIso(d: Date | null): string | null {
  if (!d) return null;
  const p = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
}

/** yyyy-MM-dd → Date local (evita el desfase de zona horaria) */
function deFechaIso(s?: string | null): Date | null {
  return s ? new Date(`${s.slice(0, 10)}T00:00:00`) : null;
}

// Formulario único para crear y editar un juego (editar si la ruta trae :id)
@Component({
  selector: 'app-grabar-videojuego',
  standalone: false,
  templateUrl: './grabar-videojuego.html',
  styleUrl: './grabar-videojuego.css',
})
export class GrabarVideojuego {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private videojuegoService = inject(VideojuegoService);
  private plataformaService = inject(PlataformaService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(true);
  readonly guardando = signal(false);
  readonly plataformas = signal<Plataforma[]>([]);
  readonly pegis = ['3', '7', '12', '16', '18'];

  private original: Videojuego | null = null;

  readonly form = this.fb.nonNullable.group({
    nombreJuego: ['', [Validators.required, Validators.maxLength(200)]],
    idPlataforma: [null as number | null, Validators.required],
    genero: ['', Validators.maxLength(100)],
    desarrollador: ['', Validators.maxLength(150)],
    fechaLanzamiento: [null as Date | null], // datepicker
    clasificacionPEGI: ['12'],
    tiempoPromedioSesion: [null as number | null, [Validators.min(0), Validators.max(1440)]],
    urlImagen: ['', Validators.maxLength(500)],
    descripcion: [''],
    activo: [true],
  });

  constructor() {
    forkJoin({
      plataformas: this.plataformaService.listar(),
      juego: this.id !== null ? this.videojuegoService.obtener(this.id) : of(null),
    }).subscribe({
      next: ({ plataformas, juego }) => {
        this.plataformas.set(plataformas);
        if (juego) {
          this.original = juego;
          this.form.patchValue({
            nombreJuego: juego.nombreJuego,
            idPlataforma: juego.idPlataforma ?? null,
            genero: juego.genero ?? '',
            desarrollador: juego.desarrollador ?? '',
            fechaLanzamiento: deFechaIso(juego.fechaLanzamiento),
            clasificacionPEGI: juego.clasificacionPEGI ?? '12',
            tiempoPromedioSesion: juego.tiempoPromedioSesion ?? null,
            urlImagen: juego.urlImagen ?? '',
            descripcion: juego.descripcion ?? '',
            activo: juego.activo ?? true,
          });
        }
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
    const juego: Videojuego = {
      ...this.original,
      nombreJuego: v.nombreJuego.trim(),
      idPlataforma: v.idPlataforma as number,
      genero: v.genero,
      desarrollador: v.desarrollador,
      fechaLanzamiento: aFechaIso(v.fechaLanzamiento) ?? undefined,
      clasificacionPEGI: v.clasificacionPEGI,
      tiempoPromedioSesion: v.tiempoPromedioSesion ?? undefined,
      urlImagen: v.urlImagen.trim() || null,
      descripcion: v.descripcion,
      activo: v.activo,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.videojuegoService.actualizar(this.id, juego)
        : this.videojuegoService.crear(juego);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Videojuego actualizado' : 'Videojuego creado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el videojuego', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/videojuegos']);
  }
}
