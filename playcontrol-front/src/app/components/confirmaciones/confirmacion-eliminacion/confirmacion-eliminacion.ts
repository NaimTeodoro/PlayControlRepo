import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

// Datos opcionales que recibe el diálogo
export interface DatosConfirmacion {
  titulo?: string;
  mensaje?: string;
  textoConfirmar?: string;
}

// Diálogo reutilizable: se abre con MatDialog antes de cualquier DELETE.
// Devuelve true si el usuario confirma.
@Component({
  selector: 'app-confirmacion-eliminacion',
  standalone: false,
  templateUrl: './confirmacion-eliminacion.html',
  styleUrl: './confirmacion-eliminacion.css',
})
export class ConfirmacionEliminacion {
  private datos = inject<DatosConfirmacion | null>(MAT_DIALOG_DATA, { optional: true });

  readonly titulo = this.datos?.titulo ?? '¿Eliminar registro?';
  readonly mensaje = this.datos?.mensaje ?? 'Esta acción no se puede deshacer.';
  readonly textoConfirmar = this.datos?.textoConfirmar ?? 'Eliminar';
}
