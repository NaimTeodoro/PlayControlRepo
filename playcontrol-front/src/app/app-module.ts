import { registerLocaleData } from '@angular/common';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import localeEsPe from '@angular/common/locales/es-PE';
import { LOCALE_ID, NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { MaterialModule } from './modules/material/material-module';
import { AuthInterceptor } from './interceptors/auth-interceptor';

import { ConfirmacionEliminacion } from './components/confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';
import { Login } from './components/auth/login/login';
import { Registro } from './components/auth/registro/registro';
import { BarraNavegacion } from './components/layout/barra-navegacion/barra-navegacion';
import { MenuLateral } from './components/layout/menu-lateral/menu-lateral';
import { InicioJugador } from './components/inicio/inicio-jugador/inicio-jugador';
import { InicioPadre } from './components/inicio/inicio-padre/inicio-padre';
import { InicioProfesional } from './components/inicio/inicio-profesional/inicio-profesional';
import { InicioAdmin } from './components/inicio/inicio-admin/inicio-admin';
import { ResumenTiempo } from './components/monitoreo/resumen-tiempo/resumen-tiempo';
import { GrabarSesion } from './components/monitoreo/grabar-sesion/grabar-sesion';
import { PerfilJugador } from './components/perfil/perfil-jugador/perfil-jugador';
import { ListaSesiones } from './components/monitoreo/lista-sesiones/lista-sesiones';
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
import { TarjetaCita } from './components/citas/tarjeta-cita/tarjeta-cita';
import { VincularHijo } from './components/familia/vincular-hijo/vincular-hijo';
import { SolicitudesVinculo } from './components/familia/solicitudes-vinculo/solicitudes-vinculo';
import { ListaReportes } from './components/reportes/lista-reportes/lista-reportes';
import { DetalleReporte } from './components/reportes/detalle-reporte/detalle-reporte';
import { PerfilPadre } from './components/perfil/perfil-padre/perfil-padre';
import { ListaClientes } from './components/profesional/lista-clientes/lista-clientes';
import { DetalleCliente } from './components/profesional/detalle-cliente/detalle-cliente';
import { GrabarCliente } from './components/profesional/grabar-cliente/grabar-cliente';
import { SelectorHora } from './components/confirmaciones/selector-hora/selector-hora';
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

// Fechas y nombres de meses/días en español (DatePipe)
registerLocaleData(localeEsPe);

@NgModule({
  declarations: [
    App,
    ConfirmacionEliminacion,
    Login,
    Registro,
    BarraNavegacion,
    MenuLateral,
    InicioJugador,
    InicioPadre,
    InicioProfesional,
    InicioAdmin,
    ResumenTiempo,
    ListaSesiones,
    GrabarSesion,
    PerfilJugador,
    ListaLimites,
    GrabarLimite,
    ListaAlertas,
    DetalleAlerta,
    ListaDispositivos,
    GrabarDispositivo,
    ListaLogros,
    Ranking,
    ListaAmigos,
    ListaFaq,
    ListaRecursos,
    DetalleRecurso,
    ListaHijos,
    DetalleHijo,
    ListaCitas,
    TarjetaCita,
    VincularHijo,
    SolicitudesVinculo,
    ListaReportes,
    DetalleReporte,
    PerfilPadre,
    ListaClientes,
    DetalleCliente,
    GrabarCliente,
    SelectorHora,
    PerfilProfesional,
    GrabarRecurso,
    ListaUsuarios,
    GrabarUsuario,
    ListaLogrosCatalogo,
    GrabarLogro,
    ListaVideojuegos,
    GrabarVideojuego,
    ListaPlataformas,
    GrabarPlataforma,
  ],
  imports: [BrowserModule, AppRoutingModule, ReactiveFormsModule, MaterialModule],
  providers: [
    provideBrowserGlobalErrorListeners(),
    // HttpClient + interceptor clásico (JWT) registrado con HTTP_INTERCEPTORS
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: LOCALE_ID, useValue: 'es-PE' },
  ],
  bootstrap: [App],
})
export class AppModule {}
