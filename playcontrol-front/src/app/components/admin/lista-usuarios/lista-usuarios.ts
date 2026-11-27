import { Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { INFO_ROLES, Rol } from '../../../models/rol';
import { Usuario } from '../../../models/usuario';
import { NotificacionService } from '../../../services/notificacion-service';
import { UsuarioService } from '../../../services/usuario-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Usuarios (solo ADMIN): lista con búsqueda y filtro por rol; alta y edición en grabar-usuario, baja con confirmación
@Component({
  selector: 'app-lista-usuarios',
  standalone: false,
  templateUrl: './lista-usuarios.html',
  styleUrl: './lista-usuarios.css',
})
export class ListaUsuarios {
  private usuarioService = inject(UsuarioService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  private usuarios = signal<Usuario[]>([]);
  readonly busqueda = signal('');
  readonly filtroRol = signal<'TODOS' | Rol>('TODOS');

  readonly columnas = ['usuario', 'email', 'rol', 'estado', 'registro', 'acceso', 'acciones'];
  readonly datos = new MatTableDataSource<Usuario>([]);
  private paginador = viewChild(MatPaginator);

  readonly roles = (Object.keys(INFO_ROLES) as Rol[]).map((valor) => ({
    valor,
    ...INFO_ROLES[valor],
  }));

  readonly filtrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();
    const rol = this.filtroRol();
    return this.usuarios().filter(
      (u) =>
        (rol === 'TODOS' || this.rolDe(u) === rol) &&
        (!texto ||
          `${u.username} ${u.nombre ?? ''} ${u.apellido ?? ''} ${u.email ?? ''}`
            .toLowerCase()
            .includes(texto)),
    );
  });

  readonly conteoPorRol = computed(() => {
    const cuenta = (rol: Rol) => this.usuarios().filter((u) => this.rolDe(u) === rol).length;
    return this.roles.map((r) => ({ ...r, total: cuenta(r.valor) }));
  });

  constructor() {
    effect(() => (this.datos.data = this.filtrados()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
    this.cargar();
  }

  private cargar(): void {
    this.cargando.set(true);
    this.usuarioService.listar().subscribe({
      next: (lista) => {
        // Orden por id (los más recientes al final) y luego por username
        this.usuarios.set([...lista].sort((a, b) => (a.idUsuario ?? 0) - (b.idUsuario ?? 0)));
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  rolDe(u: Usuario): Rol | null {
    return u.roles?.[0] ?? null;
  }

  etiquetaRol(u: Usuario): string {
    const rol = this.rolDe(u);
    return rol ? INFO_ROLES[rol].etiqueta : '—';
  }

  buscar(evento: Event): void {
    this.busqueda.set((evento.target as HTMLInputElement).value);
  }

  eliminar(u: Usuario): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar usuario?',
          mensaje: `Se eliminará la cuenta "${u.username}". Si tiene datos asociados, el sistema podría no permitirlo.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.usuarioService.eliminar(u.idUsuario as number).subscribe({
          next: () => {
            this.notificacion.exito('Usuario eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err, 'No se pudo eliminar: el usuario está en uso'),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
