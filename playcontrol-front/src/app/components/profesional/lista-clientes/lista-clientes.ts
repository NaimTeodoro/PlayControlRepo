import { Component, effect, inject, signal, viewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ProfesionalCliente } from '../../../models/profesional-cliente';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  nombreCliente,
  ProfesionalClienteService,
} from '../../../services/profesional-cliente-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Clientes del profesional (GET /profesional-clientes/profesional/{idProfesional}) con ver, editar y eliminar
@Component({
  selector: 'app-lista-clientes',
  standalone: false,
  templateUrl: './lista-clientes.html',
  styleUrl: './lista-clientes.css',
})
export class ListaClientes {
  private auth = inject(AuthService);
  private clienteService = inject(ProfesionalClienteService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  private idProfesional = this.auth.getIdProfesional();
  readonly sinPerfil = signal(this.idProfesional === null);
  readonly clientes = signal<ProfesionalCliente[]>([]);
  readonly nombreCliente = nombreCliente;

  readonly columnas = ['cliente', 'estado', 'frecuencia', 'proxima', 'diagnostico', 'acciones'];
  readonly datos = new MatTableDataSource<ProfesionalCliente>([]);
  private paginador = viewChild(MatPaginator);

  constructor() {
    effect(() => (this.datos.data = this.clientes()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
    this.cargar();
  }

  private cargar(): void {
    if (this.idProfesional === null) {
      this.cargando.set(false);
      return;
    }
    this.cargando.set(true);
    this.clienteService.porProfesional(this.idProfesional).subscribe({
      next: (clientes) => {
        this.clientes.set(clientes);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  eliminar(c: ProfesionalCliente): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar a este cliente?',
          mensaje: `Se borrará la ficha de ${nombreCliente(c)}, con su diagnóstico y notas.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.clienteService.eliminar(c.idProfesionalCliente as number).subscribe({
          next: () => {
            this.notificacion.exito('Cliente eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
