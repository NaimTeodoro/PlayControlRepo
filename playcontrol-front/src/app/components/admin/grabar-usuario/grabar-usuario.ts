import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { INFO_ROLES, Rol } from '../../../models/rol';
import { ESTADOS_USUARIO, NIVELES_TECNOLOGIA, Usuario } from '../../../models/usuario';
import { NotificacionService } from '../../../services/notificacion-service';
import { UsuarioService } from '../../../services/usuario-service';

/** Date → yyyy-MM-dd (sin zona) */
function aFechaIso(d: Date | null): string | null {
  if (!d) return null;
  const p = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
}

/** yyyy-MM-dd → Date local (evita el desfase de zona horaria) */
function deFechaIso(s?: string | null): Date | null {
  return s ? new Date(`${s.slice(0, 10)}T00:00:00`) : null;
}

// Formulario único para crear y editar un usuario (editar si la ruta trae :id).
// La contraseña es obligatoria solo al crear; al editar, vacía = se conserva la actual.
@Component({
  selector: 'app-grabar-usuario',
  standalone: false,
  templateUrl: './grabar-usuario.html',
  styleUrl: './grabar-usuario.css',
})
export class GrabarUsuario {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private usuarioService = inject(UsuarioService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);
  readonly verPassword = signal(false);

  readonly hoy = new Date();
  readonly estados = ESTADOS_USUARIO;
  readonly niveles = NIVELES_TECNOLOGIA;
  readonly generos = signal(['MASCULINO', 'FEMENINO', 'OTRO']);
  readonly roles = (Object.keys(INFO_ROLES) as Rol[]).map((valor) => ({
    valor,
    etiqueta: INFO_ROLES[valor].etiqueta,
  }));

  private original: Usuario | null = null;

  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    // Al crear es obligatoria (se ajusta abajo); al editar es opcional
    password: ['', [Validators.minLength(6)]],
    nombre: ['', Validators.required],
    apellido: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    telefono: [''],
    fechaNacimiento: [null as Date | null], // datepicker
    genero: [''],
    pais: [''],
    ciudad: [''],
    direccion: [''],
    nivelTecnologia: [''],
    rol: ['ROLE_JUGADOR' as Rol, Validators.required],
    estado: ['ACTIVO'],
    enabled: [true],
  });

  constructor() {
    if (this.id === null) {
      this.form.controls.password.addValidators(Validators.required);
      return;
    }
    this.usuarioService.obtener(this.id).subscribe({
      next: (u) => {
        this.original = u;
        // Si el género guardado no está entre las opciones comunes, se agrega para no perderlo
        if (u.genero && !this.generos().includes(u.genero)) {
          this.generos.update((l) => [...l, u.genero as string]);
        }
        this.form.patchValue({
          username: u.username,
          nombre: u.nombre ?? '',
          apellido: u.apellido ?? '',
          email: u.email ?? '',
          telefono: u.telefono ?? '',
          fechaNacimiento: deFechaIso(u.fechaNacimiento),
          genero: u.genero ?? '',
          pais: u.pais ?? '',
          ciudad: u.ciudad ?? '',
          direccion: u.direccion ?? '',
          nivelTecnologia: u.nivelTecnologia ?? '',
          rol: u.roles?.[0] ?? 'ROLE_JUGADOR',
          estado: u.estado ?? 'ACTIVO',
          enabled: u.enabled ?? true,
        });
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.notificacion.error(err);
        this.volver();
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se fusiona con el original porque PUT reemplaza el registro completo
    const usuario: Usuario = {
      ...this.original,
      username: v.username.trim(),
      nombre: v.nombre.trim(),
      apellido: v.apellido.trim(),
      email: v.email.trim(),
      telefono: v.telefono || null,
      fechaNacimiento: aFechaIso(v.fechaNacimiento),
      genero: v.genero || null,
      pais: v.pais || null,
      ciudad: v.ciudad || null,
      direccion: v.direccion || null,
      nivelTecnologia: (v.nivelTecnologia || null) as Usuario['nivelTecnologia'],
      estado: v.estado as Usuario['estado'],
      enabled: v.enabled,
      roles: [v.rol],
    };
    // Solo se envía la contraseña si se escribió (nunca se devuelve del servidor)
    if (v.password) usuario.password = v.password;
    else delete usuario.password;

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.usuarioService.actualizar(this.id, usuario)
        : this.usuarioService.crear(usuario);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Usuario actualizado' : 'Usuario creado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        // 409: username o email ya registrados; 400: errores por campo
        this.notificacion.error(err, 'No se pudo guardar el usuario', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/usuarios']);
  }
}
