import { Component, computed, input } from '@angular/core';
import { Cita } from '../../../models/cita';
import {
  diasHasta,
  etiquetaFaltan,
  fechaCita,
  nombreProfesional,
} from '../../../services/cita-service';

// Tarjeta de solo lectura de una cita (se usa en "Mis citas" y en el detalle del hijo)
@Component({
  selector: 'app-tarjeta-cita',
  standalone: false,
  templateUrl: './tarjeta-cita.html',
  styleUrl: './tarjeta-cita.css',
})
export class TarjetaCita {
  readonly cita = input.required<Cita>();
  /** Resalta la cita más próxima */
  readonly destacada = input(false);

  readonly fecha = computed(() => fechaCita(this.cita()));
  readonly profesional = computed(() => nombreProfesional(this.cita()));
  readonly etiqueta = computed(() => etiquetaFaltan(this.cita()));
  /** Clase del chip: verde si es próxima, roja si la fecha ya pasó */
  readonly claseChip = computed(() => {
    const dias = diasHasta(this.cita().proximaConsulta);
    if (dias === null) return '';
    return dias < 0 ? 'peligro' : 'exito';
  });
}
