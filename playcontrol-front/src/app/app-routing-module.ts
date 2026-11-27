import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { authGuard, invitadoGuard } from './guards/auth-guard';
import { limiteControlGuard } from './guards/limite-control-guard';
import { rolGuard } from './guards/rol-guard';
import { Rol } from './models/rol';

import { Login } from './components/auth/login/login';
import { Registro } from './components/auth/registro/registro';
import { InicioJugador } from './components/inicio/inicio-jugador/inicio-jugador';
import { InicioPadre } from './components/inicio/inicio-padre/inicio-padre';
import { InicioProfesional } from './components/inicio/inicio-profesional/inicio-profesional';
import { InicioAdmin } from './components/inicio/inicio-admin/inicio-admin';
import { ResumenTiempo } from './components/monitoreo/resumen-tiempo/resumen-tiempo';
import { GrabarSesion } from './components/monitoreo/grabar-sesion/grabar-sesion';
import { PerfilJugador } from './components/perfil/perfil-jugador/perfil-jugador';
import { ListaLimites } from './components/limites/lista-limites/lista-limites';
import { GrabarLimite } from './components/limites/grabar-limite/grabar-limite';
import { ListaAlertas } from './components/alertas/lista-alertas/lista-alertas';
import { DetalleAlerta } from './components/alertas/detalle-alerta/detalle-alerta';
import { ListaDispositivos } from './components/dispositivos/lista-dispositivos/lista-dispositivos';
import { GrabarDispositivo } from './components/dispositivos/grabar-dispositivo/grabar-dispositivo';
import { ListaLogros } from './components/logros/lista-logros/lista-logros';
import { Ranking } from './components/logros/ranking/ranking';
import { ListaAmigos } from './components/amigos/lista-amigos/lista-amigos';
import { ListaFaq } from './components/soporte/lista-faq/lista-faq';
import { ListaRecursos } from './components/recursos/lista-recursos/lista-recursos';
import { DetalleRecurso } from './components/recursos/detalle-recurso/detalle-recurso';
import { ListaHijos } from './components/familia/lista-hijos/lista-hijos';
import { DetalleHijo } from './components/familia/detalle-hijo/detalle-hijo';
import { ListaCitas } from './components/citas/lista-citas/lista-citas';
import { VincularHijo } from './components/familia/vincular-hijo/vincular-hijo';
import { SolicitudesVinculo } from './components/familia/solicitudes-vinculo/solicitudes-vinculo';
import { ListaReportes } from './components/reportes/lista-reportes/lista-reportes';
import { DetalleReporte } from './components/reportes/detalle-reporte/detalle-reporte';
import { PerfilPadre } from './components/perfil/perfil-padre/perfil-padre';
import { ListaClientes } from './components/profesional/lista-clientes/lista-clientes';
import { DetalleCliente } from './components/profesional/detalle-cliente/detalle-cliente';
import { GrabarCliente } from './components/profesional/grabar-cliente/grabar-cliente';
import { PerfilProfesional } from './components/perfil/perfil-profesional/perfil-profesional';
import { GrabarRecurso } from './components/recursos/grabar-recurso/grabar-recurso';
import { ListaUsuarios } from './components/admin/lista-usuarios/lista-usuarios';
import { ListaVideojuegos } from './components/admin/lista-videojuegos/lista-videojuegos';
import { GrabarVideojuego } from './components/admin/grabar-videojuego/grabar-videojuego';
import { GrabarUsuario } from './components/admin/grabar-usuario/grabar-usuario';
import { ListaLogrosCatalogo } from './components/admin/lista-logros-catalogo/lista-logros-catalogo';
import { GrabarLogro } from './components/admin/grabar-logro/grabar-logro';
import { ListaPlataformas } from './components/admin/lista-plataformas/lista-plataformas';
import { GrabarPlataforma } from './components/admin/grabar-plataforma/grabar-plataforma';

/** Atajo para proteger una ruta por rol(es) */
const soloRoles = (...roles: Rol[]) => ({ canActivate: [authGuard, rolGuard], data: { roles } });

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },

  // Públicas (si ya hay sesión, el guard redirige al inicio del rol)
  {
    path: 'login',
    component: Login,
    canActivate: [invitadoGuard],
    title: 'Iniciar sesión · PlayControl',
  },
  {
    path: 'registro',
    component: Registro,
    canActivate: [invitadoGuard],
    title: 'Registro · PlayControl',
  },

  // Inicios por rol
  {
    path: 'inicio-jugador',
    component: InicioJugador,
    title: 'Inicio · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'inicio-padre',
    component: InicioPadre,
    title: 'Inicio · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'inicio-profesional',
    component: InicioProfesional,
    title: 'Inicio · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'inicio-admin',
    component: InicioAdmin,
    title: 'Inicio · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },

  // Jugador (Fase 2)
  {
    path: 'monitoreo',
    component: ResumenTiempo,
    title: 'Monitoreo · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'monitoreo/registrar',
    component: GrabarSesion,
    title: 'Registrar sesión · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'monitoreo/sesion/:id',
    component: GrabarSesion,
    title: 'Editar sesión · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'perfil-jugador',
    component: PerfilJugador,
    title: 'Mi perfil · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'limites',
    component: ListaLimites,
    title: 'Mis límites · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'limites/nuevo',
    component: GrabarLimite,
    title: 'Nuevo límite · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE'),
    canActivate: [authGuard, rolGuard, limiteControlGuard],
  },
  {
    path: 'limites/editar/:id',
    component: GrabarLimite,
    title: 'Editar límite · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE'),
    canActivate: [authGuard, rolGuard, limiteControlGuard],
  },
  {
    path: 'alertas',
    component: ListaAlertas,
    title: 'Alertas · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'alertas/:id',
    component: DetalleAlerta,
    title: 'Alerta · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'dispositivos',
    component: ListaDispositivos,
    title: 'Dispositivos · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'dispositivos/nuevo',
    component: GrabarDispositivo,
    title: 'Nuevo dispositivo · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'dispositivos/editar/:id',
    component: GrabarDispositivo,
    title: 'Editar dispositivo · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },

  // Logros, soporte y recursos (Fase 3)
  {
    path: 'logros',
    component: ListaLogros,
    title: 'Logros · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'ranking',
    component: Ranking,
    title: 'Ranking · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'amigos',
    component: ListaAmigos,
    title: 'Amigos · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'solicitudes',
    component: SolicitudesVinculo,
    title: 'Solicitudes · PlayControl',
    ...soloRoles('ROLE_JUGADOR'),
  },
  {
    path: 'soporte',
    component: ListaFaq,
    title: 'Soporte · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE'),
  },

  // Profesional (Fase 5). Las rutas fijas (nuevo/editar) van antes que :id
  {
    path: 'clientes',
    component: ListaClientes,
    title: 'Clientes · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'clientes/nuevo',
    component: GrabarCliente,
    title: 'Nuevo cliente · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'clientes/editar/:id',
    component: GrabarCliente,
    title: 'Editar cliente · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'clientes/:id',
    component: DetalleCliente,
    title: 'Cliente · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'perfil-profesional',
    component: PerfilProfesional,
    title: 'Mi perfil · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL'),
  },
  {
    path: 'recursos/nuevo',
    component: GrabarRecurso,
    title: 'Nuevo recurso · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL', 'ROLE_ADMIN'),
  },
  {
    path: 'recursos/editar/:id',
    component: GrabarRecurso,
    title: 'Editar recurso · PlayControl',
    ...soloRoles('ROLE_PROFESIONAL', 'ROLE_ADMIN'),
  },
  {
    path: 'recursos',
    component: ListaRecursos,
    title: 'Recursos · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE', 'ROLE_PROFESIONAL', 'ROLE_ADMIN'),
  },
  {
    path: 'recursos/:id',
    component: DetalleRecurso,
    title: 'Recurso · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE', 'ROLE_PROFESIONAL', 'ROLE_ADMIN'),
  },

  // Citas con profesionales (V4): jugador y padre, solo lectura
  {
    path: 'citas',
    component: ListaCitas,
    title: 'Citas · PlayControl',
    ...soloRoles('ROLE_JUGADOR', 'ROLE_PADRE'),
  },

  // Padre (Fase 4)
  {
    path: 'familia',
    component: ListaHijos,
    title: 'Mis hijos · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'familia/vincular',
    component: VincularHijo,
    title: 'Vincular hijo · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'familia/hijo/:id',
    component: DetalleHijo,
    title: 'Detalle del hijo · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'reportes',
    component: ListaReportes,
    title: 'Reportes · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'reportes/:id',
    component: DetalleReporte,
    title: 'Reporte · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },
  {
    path: 'perfil-padre',
    component: PerfilPadre,
    title: 'Mi perfil · PlayControl',
    ...soloRoles('ROLE_PADRE'),
  },

  // Admin (Fase 6)
  {
    path: 'usuarios',
    component: ListaUsuarios,
    title: 'Usuarios · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'usuarios/nuevo',
    component: GrabarUsuario,
    title: 'Nuevo usuario · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'usuarios/editar/:id',
    component: GrabarUsuario,
    title: 'Editar usuario · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'logros-catalogo',
    component: ListaLogrosCatalogo,
    title: 'Catálogo de logros · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'logros-catalogo/nuevo',
    component: GrabarLogro,
    title: 'Nuevo logro · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'logros-catalogo/editar/:id',
    component: GrabarLogro,
    title: 'Editar logro · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'videojuegos',
    component: ListaVideojuegos,
    title: 'Videojuegos · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'videojuegos/nuevo',
    component: GrabarVideojuego,
    title: 'Nuevo videojuego · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'videojuegos/editar/:id',
    component: GrabarVideojuego,
    title: 'Editar videojuego · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'plataformas',
    component: ListaPlataformas,
    title: 'Plataformas · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'plataformas/nueva',
    component: GrabarPlataforma,
    title: 'Nueva plataforma · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },
  {
    path: 'plataformas/editar/:id',
    component: GrabarPlataforma,
    title: 'Editar plataforma · PlayControl',
    ...soloRoles('ROLE_ADMIN'),
  },

  // Cualquier otra ruta → login (que a su vez manda al inicio si hay sesión)
  { path: '**', redirectTo: 'login' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
