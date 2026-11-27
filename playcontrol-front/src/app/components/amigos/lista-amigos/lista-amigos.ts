import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroupDirective, Validators } from '@angular/forms';
import { NotificacionService } from '../../../services/notificacion-service';

interface Amigo {
  nombre: string;
  enLinea: boolean;
}

// SIMULADO: el backend no tiene amigos. Lista y solicitudes solo existen en esta pantalla.
const AMIGOS_SIMULADOS: Amigo[] = [
  { nombre: 'AtomicS', enLinea: true },
  { nombre: 'Dr_Simon777', enLinea: false },
  { nombre: 'Arrostrico_Yt', enLinea: true },
  { nombre: 'T1_LuisVives', enLinea: false },
];

@Component({
  selector: 'app-lista-amigos',
  standalone: false,
  templateUrl: './lista-amigos.html',
  styleUrl: './lista-amigos.css',
})
export class ListaAmigos {
  private notificacion = inject(NotificacionService);

  readonly amigos = signal<Amigo[]>(AMIGOS_SIMULADOS);

  readonly form = inject(FormBuilder).nonNullable.group({
    usuario: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
  });

  // SIMULADO: la solicitud no se envía a ningún servidor
  agregar(directiva: FormGroupDirective): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const nombre = this.form.getRawValue().usuario.trim();
    this.amigos.update((lista) => [{ nombre, enLinea: false }, ...lista]);
    directiva.resetForm({ usuario: '' });
    this.notificacion.exito(`Solicitud enviada a ${nombre} (simulado)`);
  }
}
