import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { Dispositivo } from '../../../models/dispositivo';
import {
  EnergiaFisica,
  ESTADOS_ANIMO,
  EstadoAnimo,
  NIVELES_ENERGIA,
  SesionJuego,
} from '../../../models/sesion-juego';
import { Videojuego } from '../../../models/videojuego';
import { AuthService } from '../../../services/auth-service';
import { DispositivoService } from '../../../services/dispositivo-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  formatoDuracion,
  isoLocal,
  minutosSesion,
  SesionService,
} from '../../../services/sesion-service';
import { VideojuegoService } from '../../../services/videojuego-service';

type Modo = 'iniciar' | 'terminar' | 'editar';

// Registrar sesión: iniciar (POST EN_CURSO), terminar (PUT FINALIZADA + bienestar) o editar el
// bienestar de una sesión ya registrada (/monitoreo/sesion/:id). Solo una EN_CURSO por usuario.
@Component({
  selector: 'app-grabar-sesion',
  standalone: false,
  templateUrl: './grabar-sesion.html',
  styleUrl: './grabar-sesion.css',
})
export class GrabarSesion {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private auth = inject(AuthService);
  private sesionService = inject(SesionService);
  private videojuegoService = inject(VideojuegoService);
  private dispositivoService = inject(DispositivoService);
  private notificacion = inject(NotificacionService);

  private readonly idRuta = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly guardando = signal(false);
  readonly modo = signal<Modo>('iniciar');
  readonly sesion = signal<SesionJuego | null>(null);
  readonly juegos = signal<Videojuego[]>([]);
  readonly dispositivos = signal<Dispositivo[]>([]);

  readonly energias = NIVELES_ENERGIA;
  readonly animos = ESTADOS_ANIMO;
  readonly formatoDuracion = formatoDuracion;
  readonly minutos = computed(() => {
    const s = this.sesion();
    return s ? minutosSesion(s) : 0;
  });

  readonly titulo = computed(
    () =>
      ({ iniciar: 'Registrar sesión', terminar: 'Terminar sesión', editar: 'Editar bienestar' })[
        this.modo()
      ],
  );

  readonly form = this.fb.nonNullable.group({
    idVideojuego: [null as number | null, Validators.required],
    idDispositivo: [null as number | null],
    energiaFisica: [null as EnergiaFisica | null],
    estadoAnimo: [null as EstadoAnimo | null],
    numeroInterrupciones: [null as number | null, [Validators.min(0), Validators.max(100)]],
    anotacionesUsuario: ['', Validators.maxLength(500)],
    efectosPostJuego: ['', Validators.maxLength(500)],
  });

  constructor() {
    const idUsuario = this.auth.getIdUsuario() as number;
    forkJoin({
      juegos: this.videojuegoService.listar(),
      dispositivos: this.dispositivoService.porUsuario(idUsuario),
      // Editando se pide la sesión; si no, el resumen dice si hay una EN_CURSO
      sesion: this.idRuta !== null ? this.sesionService.obtener(this.idRuta) : of(null),
      resumen: this.idRuta === null ? this.sesionService.resumen(idUsuario) : of(null),
    }).subscribe({
      next: ({ juegos, dispositivos, sesion, resumen }) => {
        this.juegos.set(juegos.filter((j) => j.activo !== false));
        this.dispositivos.set(dispositivos.filter((d) => d.estado !== 'INACTIVO'));
        const actual = sesion ?? resumen?.sesionEnCurso ?? null;
        if (actual) {
          this.sesion.set(actual);
          this.modo.set(sesion ? 'editar' : 'terminar');
          this.form.patchValue({
            idVideojuego: actual.idVideojuego,
            idDispositivo: actual.idDispositivo ?? null,
            energiaFisica: actual.energiaFisica ?? null,
            estadoAnimo: actual.estadoAnimo ?? null,
            numeroInterrupciones: actual.numeroInterrupciones ?? null,
            anotacionesUsuario: actual.anotacionesUsuario ?? '',
            efectosPostJuego: actual.efectosPostJuego ?? '',
          });
        }
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const modo = this.modo();
    const original = this.sesion();
    const bienestar = {
      energiaFisica: v.energiaFisica,
      estadoAnimo: v.estadoAnimo,
      numeroInterrupciones: v.numeroInterrupciones,
      anotacionesUsuario: v.anotacionesUsuario.trim() || null,
      efectosPostJuego: v.efectosPostJuego.trim() || null,
    };

    let peticion;
    if (modo === 'iniciar') {
      // Inicio: solo juego y dispositivo; el bienestar se registra al terminar
      peticion = this.sesionService.crear({
        idUsuario: this.auth.getIdUsuario() as number,
        idVideojuego: v.idVideojuego as number,
        idDispositivo: v.idDispositivo,
        fechaInicio: isoLocal(),
        estadoSesion: 'EN_CURSO',
      });
    } else if (modo === 'terminar' && original?.idSesion) {
      // PUT reemplaza el registro: se fusiona con la sesión en curso; el servidor calcula la duración
      peticion = this.sesionService.actualizar(original.idSesion, {
        ...original,
        ...bienestar,
        fechaFin: isoLocal(),
        duracionMinutos: null,
        estadoSesion: 'FINALIZADA',
      });
    } else if (original?.idSesion) {
      peticion = this.sesionService.actualizar(original.idSesion, { ...original, ...bienestar });
    } else {
      return;
    }

    this.guardando.set(true);
    peticion.subscribe({
      next: () => {
        const mensajes = {
          iniciar: '¡Sesión iniciada! Que lo disfrutes',
          terminar: 'Sesión finalizada',
          editar: 'Bienestar actualizado',
        };
        this.notificacion.exito(mensajes[modo]);
        this.router.navigate([modo === 'iniciar' ? '/inicio-jugador' : '/monitoreo']);
      },
      error: (err) => {
        this.guardando.set(false);
        // 409: ya hay una sesión EN_CURSO
        this.notificacion.error(err, 'No se pudo guardar la sesión', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/monitoreo']);
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
