import { Component, computed, inject, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface DatosSelectorHora {
  hora: Date | null;
}

interface Marca {
  valor: number;
  texto: string;
  x: number;
  y: number;
}

// Diálogo reutilizable con reloj: primero se elige la hora y luego los minutos.
// Devuelve un Date (solo importan hora y minutos) o undefined si se cancela.
@Component({
  selector: 'app-selector-hora',
  standalone: false,
  templateUrl: './selector-hora.html',
  styleUrl: './selector-hora.css',
})
export class SelectorHora {
  private ref = inject(MatDialogRef<SelectorHora, Date>);
  private datos = inject<DatosSelectorHora | null>(MAT_DIALOG_DATA, { optional: true });

  private inicial = this.datos?.hora ?? new Date();
  readonly modo = signal<'hora' | 'minuto'>('hora');
  readonly pm = signal(this.inicial.getHours() >= 12);
  readonly hora12 = signal(this.inicial.getHours() % 12 || 12);
  readonly minuto = signal(this.datos?.hora ? this.inicial.getMinutes() : 0);

  private static readonly C = 120; // centro del reloj (viewBox 240)
  private static readonly R = 90; // radio donde van los números

  private punto(grados: number, radio: number): { x: number; y: number } {
    const rad = ((grados - 90) * Math.PI) / 180;
    return { x: SelectorHora.C + radio * Math.cos(rad), y: SelectorHora.C + radio * Math.sin(rad) };
  }

  readonly marcas = computed<Marca[]>(() =>
    this.modo() === 'hora'
      ? Array.from({ length: 12 }, (_, i) => {
          const h = i + 1;
          return { valor: h, texto: String(h), ...this.punto(h * 30, SelectorHora.R) };
        })
      : Array.from({ length: 12 }, (_, i) => {
          const m = i * 5;
          return { valor: m, texto: String(m).padStart(2, '0'), ...this.punto(m * 6, SelectorHora.R) };
        }),
  );

  /** Extremo de la aguja según lo seleccionado */
  readonly aguja = computed(() =>
    this.modo() === 'hora'
      ? this.punto((this.hora12() % 12) * 30, SelectorHora.R)
      : this.punto(this.minuto() * 6, SelectorHora.R),
  );

  readonly textoHora = computed(() => String(this.hora12()).padStart(2, '0'));
  readonly textoMinuto = computed(() => String(this.minuto()).padStart(2, '0'));

  private arrastrando = false;

  /** Convierte la posición del puntero en hora o minuto */
  private elegir(ev: PointerEvent): void {
    const svg = ev.currentTarget as SVGElement;
    const caja = svg.getBoundingClientRect();
    const dx = ev.clientX - (caja.left + caja.width / 2);
    const dy = ev.clientY - (caja.top + caja.height / 2);
    const grados = ((Math.atan2(dy, dx) * 180) / Math.PI + 90 + 360) % 360;
    if (this.modo() === 'hora') {
      this.hora12.set(Math.round(grados / 30) % 12 || 12);
    } else {
      this.minuto.set(Math.round(grados / 6) % 60);
    }
  }

  inicio(ev: PointerEvent): void {
    this.arrastrando = true;
    (ev.currentTarget as SVGElement).setPointerCapture(ev.pointerId);
    this.elegir(ev);
  }

  mover(ev: PointerEvent): void {
    if (this.arrastrando) this.elegir(ev);
  }

  /** Al soltar la hora se pasa solo a los minutos */
  fin(): void {
    if (!this.arrastrando) return;
    this.arrastrando = false;
    if (this.modo() === 'hora') this.modo.set('minuto');
  }

  aceptar(): void {
    const d = new Date();
    d.setHours((this.hora12() % 12) + (this.pm() ? 12 : 0), this.minuto(), 0, 0);
    this.ref.close(d);
  }

  cancelar(): void {
    this.ref.close();
  }
}
