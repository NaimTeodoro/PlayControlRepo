import { Component, computed, effect, input, signal, viewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import {
  ESTADOS_ANIMO,
  ESTADOS_SESION,
  NIVELES_ENERGIA,
  SesionJuego,
} from '../../../models/sesion-juego';
import { fechaLocal, formatoDuracion, minutosSesion } from '../../../services/sesion-service';

type Periodo = 'todas' | 'hoy' | 'semana';

// Tabla de sesiones de juego (recibe la lista ya filtrada por usuario)
@Component({
  selector: 'app-lista-sesiones',
  standalone: false,
  templateUrl: './lista-sesiones.html',
  styleUrl: './lista-sesiones.css',
})
export class ListaSesiones {
  readonly sesiones = input.required<SesionJuego[]>();
  readonly titulo = input('Mis sesiones');

  readonly columnas = ['fecha', 'juego', 'duracion', 'estado', 'limite', 'bienestar', 'acciones'];
  readonly periodo = signal<Periodo>('todas');
  readonly datos = new MatTableDataSource<SesionJuego>([]);
  private paginador = viewChild(MatPaginator);

  readonly formatoDuracion = formatoDuracion;
  readonly minutos = minutosSesion;

  // Sesiones según el periodo elegido (hoy / desde el lunes / todas)
  readonly filtradas = computed(() => {
    const periodo = this.periodo();
    if (periodo === 'todas') return this.sesiones();

    const ahora = new Date();
    const hoy = fechaLocal(ahora);
    const lunes = new Date(ahora);
    lunes.setDate(ahora.getDate() - ((ahora.getDay() || 7) - 1));
    const desde = periodo === 'hoy' ? hoy : fechaLocal(lunes);
    return this.sesiones().filter((s) => s.fechaInicio.slice(0, 10) >= desde);
  });

  constructor() {
    effect(() => (this.datos.data = this.filtradas()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
  }

  claseEstado(s: SesionJuego): string {
    if (s.estadoSesion === 'EN_CURSO') return 'alerta';
    return s.estadoSesion === 'INTERRUMPIDA' ? '' : 'exito';
  }

  etiquetaEstado(s: SesionJuego): string {
    return ESTADOS_SESION.find((e) => e.valor === s.estadoSesion)?.etiqueta ?? '—';
  }

  animo(s: SesionJuego): { etiqueta: string; icono: string } | null {
    return ESTADOS_ANIMO.find((e) => e.valor === s.estadoAnimo) ?? null;
  }

  energia(s: SesionJuego): { etiqueta: string; icono: string } | null {
    return NIVELES_ENERGIA.find((e) => e.valor === s.energiaFisica) ?? null;
  }
}
