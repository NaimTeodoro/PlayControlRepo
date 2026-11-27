import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProblemDetail } from '../models/problem-detail';

/**
 * Mensaje legible de un error del backend (ProblemDetail).
 * Devuelve null cuando no hay que mostrar nada (401 ya manejado por el interceptor).
 */
export function mensajeError(err: unknown, porDefecto = 'Ocurrió un error'): string | null {
  if (typeof err === 'string') return err;
  if (!(err instanceof HttpErrorResponse)) return porDefecto;

  const cuerpo = err.error as ProblemDetail | null;
  if (err.status === 401 && !err.url?.includes('/auth/')) return null;
  if (err.status === 0) return 'No se pudo conectar con el servidor. ¿Está corriendo el backend?';
  if (err.status === 403) return cuerpo?.detail ?? 'No tienes permiso para realizar esta acción';
  if (err.status === 400 && cuerpo?.errores) {
    const errores = Object.entries(cuerpo.errores);
    if (errores.length === 1) return `${errores[0][0]}: ${errores[0][1]}`;
    return 'Revisa los campos marcados en el formulario';
  }
  if (cuerpo?.detail) return cuerpo.detail;
  if (err.status === 404) return 'No se encontró lo que buscas';
  if (err.status >= 500) return 'El servidor tuvo un problema. Inténtalo de nuevo en unos minutos';
  return porDefecto;
}

/**
 * Coloca los errores por campo de un 400 de validación (`errores: {campo: mensaje}`) en los
 * controles del formulario con la clave `servidor`, para mostrarlos con <mat-error>.
 * Se borran solos al editar el campo. Devuelve true si marcó algún campo.
 */
export function marcarErroresCampo(form: FormGroup, err: unknown): boolean {
  if (!(err instanceof HttpErrorResponse) || err.status !== 400) return false;
  const errores = (err.error as ProblemDetail | null)?.errores ?? {};
  let marcado = false;
  for (const [campo, mensaje] of Object.entries(errores)) {
    const control: AbstractControl | null = form.get(campo);
    if (control) {
      control.setErrors({ ...control.errors, servidor: mensaje });
      control.markAsTouched();
      marcado = true;
    }
  }
  return marcado;
}

// Muestra mensajes con MatSnackBar (éxito / error del backend)
@Injectable({ providedIn: 'root' })
export class NotificacionService {
  private snackBar = inject(MatSnackBar);

  exito(mensaje: string): void {
    this.snackBar.open(mensaje, 'OK', { duration: 3500, panelClass: 'snack-exito' });
  }

  /** Snackbar con el detalle del error; si se pasa el formulario, marca los campos con error */
  error(err: unknown, porDefecto = 'Ocurrió un error', form?: FormGroup): void {
    if (form) marcarErroresCampo(form, err);
    const mensaje = mensajeError(err, porDefecto);
    if (mensaje === null) return;
    this.snackBar.open(mensaje, 'Cerrar', { duration: 5000, panelClass: 'snack-error' });
  }
}
