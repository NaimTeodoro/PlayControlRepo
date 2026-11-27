import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroupDirective, Validators } from '@angular/forms';
import { RecursoEducativo } from '../../../models/recurso-educativo';
import { NotificacionService } from '../../../services/notificacion-service';
import { RecursoService } from '../../../services/recurso-service';

// Soporte: preguntas frecuentes (GET /recursos-educativos?categoria=FAQ) y formulario de contacto SIMULADO
@Component({
  selector: 'app-lista-faq',
  standalone: false,
  templateUrl: './lista-faq.html',
  styleUrl: './lista-faq.css',
})
export class ListaFaq {
  private recursoService = inject(RecursoService);
  private notificacion = inject(NotificacionService);
  private fb = inject(FormBuilder);

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly faqs = signal<RecursoEducativo[]>([]);

  readonly form = this.fb.nonNullable.group({
    asunto: ['', [Validators.required, Validators.maxLength(100)]],
    mensaje: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
  });

  constructor() {
    this.recursoService.listar({ categoria: 'FAQ' }).subscribe({
      next: (lista) => {
        this.faqs.set(lista.filter((r) => r.activo !== false));
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  // SIMULADO: el backend no tiene endpoint de contacto
  enviar(directiva: FormGroupDirective): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    directiva.resetForm({ asunto: '', mensaje: '' });
    this.notificacion.exito('¡Mensaje enviado! Te responderemos pronto (simulado)');
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
