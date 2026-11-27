import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { DIAS_SEMANA, JugadorJoven } from '../../../models/jugador-joven';
import { AuthService } from '../../../services/auth-service';
import { JugadorJovenService } from '../../../services/jugador-joven-service';
import { NotificacionService } from '../../../services/notificacion-service';

/** "21:30:00" → "21:30" (lo que espera input type=time) */
const aHoraCorta = (h?: string | null): string => (h ? h.slice(0, 5) : '');
/** "21:30" → "21:30:00" (formato del backend) */
const aHoraLarga = (h: string): string | null => (h ? `${h.slice(0, 5)}:00` : null);

// Perfil del jugador: datos de la cuenta, código de vinculación (con botón copiar) y preferencias
// editables con PUT /jugadores-jovenes/{id}
@Component({
  selector: 'app-perfil-jugador',
  standalone: false,
  templateUrl: './perfil-jugador.html',
  styleUrl: './perfil-jugador.css',
})
export class PerfilJugador {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private jugadorService = inject(JugadorJovenService);
  private notificacion = inject(NotificacionService);

  readonly nombre = this.auth.getNombreCompleto();
  readonly username = this.auth.getUsername();
  readonly inicial = (this.nombre || '?').charAt(0).toUpperCase();
  readonly dias = DIAS_SEMANA;

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly guardando = signal(false);
  readonly editando = signal(false);
  readonly perfil = signal<JugadorJoven | null>(null);

  readonly form = this.fb.nonNullable.group({
    horaPreferidaInicio: [''],
    horaPreferidaFin: [''],
    diasJuegoPreferidos: [[] as string[]],
    generoPreferido: ['', Validators.maxLength(100)],
    motivacionPrincipal: ['', Validators.maxLength(300)],
  });

  constructor() {
    this.jugadorService.porUsuario(this.auth.getIdUsuario() as number).subscribe({
      next: (p) => {
        this.perfil.set(p);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  /** "LUN,MAR" → ["LUN","MAR"] */
  private aLista(valor?: string | null): string[] {
    return valor ? valor.split(',').filter((d) => !!d) : [];
  }

  /** Etiquetas de los días para mostrarlos en lectura */
  etiquetasDias(valor?: string | null): string[] {
    return this.aLista(valor).map((d) => DIAS_SEMANA.find((x) => x.valor === d)?.etiqueta ?? d);
  }

  copiarCodigo(): void {
    const codigo = this.perfil()?.codigoVinculacion;
    if (!codigo) return;
    navigator.clipboard
      ?.writeText(codigo)
      .then(() => this.notificacion.exito('Código copiado'))
      .catch(() => this.notificacion.error('No se pudo copiar el código'));
  }

  editar(): void {
    const p = this.perfil();
    if (!p) return;
    this.form.patchValue({
      horaPreferidaInicio: aHoraCorta(p.horaPreferidaInicio),
      horaPreferidaFin: aHoraCorta(p.horaPreferidaFin),
      diasJuegoPreferidos: this.aLista(p.diasJuegoPreferidos),
      generoPreferido: p.generoPreferido ?? '',
      motivacionPrincipal: p.motivacionPrincipal ?? '',
    });
    this.editando.set(true);
  }

  cancelar(): void {
    this.editando.set(false);
  }

  guardar(): void {
    const original = this.perfil();
    if (this.form.invalid || !original?.idJugadorJoven) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se fusiona con el original porque PUT reemplaza el registro (el código no cambia)
    const datos: JugadorJoven = {
      ...original,
      horaPreferidaInicio: aHoraLarga(v.horaPreferidaInicio),
      horaPreferidaFin: aHoraLarga(v.horaPreferidaFin),
      // Orden fijo de la semana, sin espacios: "LUN,MAR,..."
      diasJuegoPreferidos:
        DIAS_SEMANA.map((d) => d.valor)
          .filter((d) => v.diasJuegoPreferidos.includes(d))
          .join(',') || null,
      generoPreferido: v.generoPreferido.trim() || null,
      motivacionPrincipal: v.motivacionPrincipal.trim() || null,
    };

    this.guardando.set(true);
    this.jugadorService.actualizar(original.idJugadorJoven, datos).subscribe({
      next: (guardado) => {
        this.perfil.set(guardado);
        this.guardando.set(false);
        this.editando.set(false);
        this.notificacion.exito('Perfil actualizado');
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el perfil', this.form);
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
