import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(false);
  readonly verPassword = signal(false);

  // Si viene del registro, se precarga el username (?usuario=...)
  readonly form = this.fb.nonNullable.group({
    username: [
      inject(ActivatedRoute).snapshot.queryParamMap.get('usuario') ?? '',
      Validators.required,
    ],
    password: ['', Validators.required],
  });

  ingresar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { username, password } = this.form.getRawValue();
    this.cargando.set(true);

    this.auth.login(username.trim(), password).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigateByUrl(this.auth.rutaInicio());
      },
      error: (err) => {
        this.cargando.set(false);
        this.notificacion.error(err, 'Usuario o contraseña incorrectos');
      },
    });
  }
}
