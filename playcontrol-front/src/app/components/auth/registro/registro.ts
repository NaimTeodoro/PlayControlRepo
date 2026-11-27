import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistroUsuario } from '../../../models/registro-usuario';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';

type RolRegistro = RegistroUsuario['rol'];

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(false);
  readonly verPassword = signal(false);

  // ROLE_ADMIN nunca se ofrece en el registro
  readonly roles: { valor: RolRegistro; etiqueta: string; icono: string }[] = [
    { valor: 'ROLE_JUGADOR', etiqueta: 'Jugador', icono: 'sports_esports' },
    { valor: 'ROLE_PADRE', etiqueta: 'Padre / Tutor', icono: 'family_restroom' },
    { valor: 'ROLE_PROFESIONAL', etiqueta: 'Profesional', icono: 'psychology' },
  ];

  readonly form = this.fb.nonNullable.group({
    nombre: ['', Validators.required],
    apellido: ['', Validators.required],
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    rol: ['ROLE_JUGADOR' as RolRegistro, Validators.required],
  });

  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const datos: RegistroUsuario = this.form.getRawValue();
    datos.username = datos.username.trim();
    this.cargando.set(true);

    this.auth.registrar(datos).subscribe({
      next: () => {
        this.cargando.set(false);
        // El backend crea el perfil del rol; el jugador recibe su código de vinculación
        this.notificacion.exito(
          datos.rol === 'ROLE_JUGADOR'
            ? '¡Cuenta creada! Al entrar verás tu código para vincularte con tu familia.'
            : '¡Cuenta creada! Ahora inicia sesión.',
        );
        this.router.navigate(['/login'], { queryParams: { usuario: datos.username } });
      },
      error: (err) => {
        this.cargando.set(false);
        // 409: usuario o email ya registrados; 400: errores por campo
        this.notificacion.error(err, 'No se pudo crear la cuenta', this.form);
      },
    });
  }
}
