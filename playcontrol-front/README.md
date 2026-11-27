# PlayControl · Front (Angular)

Front de **PlayControl**, app para prevenir la adicción a videojuegos en jóvenes (proyecto UPC).
Angular 22 con NgModules y Angular Material. Consume el backend Spring Boot V2
(contrato en [docs/api-contract.md](docs/api-contract.md)).

## Cómo ejecutarlo

1. **Backend** corriendo en `http://localhost:8080` (Swagger: `/swagger-ui.html`).
   Solo acepta CORS desde `http://localhost:4200`, así que el front debe servirse en ese puerto.
2. `npm install` (solo la primera vez)
3. `npm start` y abrir <http://localhost:4200>

Otros comandos: `npm run build` (producción, en `dist/`) · `npm test` (Vitest).
La URL del backend está en [src/environments/environment.ts](src/environments/environment.ts).

## Cuentas de prueba (clave `123456`)

| Rol | Usuarios | Qué mirar |
|---|---|---|
| Jugador | `jugador1` … `jugador8` | `jugador2` superó su límite; `jugador3` tiene una sesión en curso; `jugador8` lidera el ranking |
| Padre/Tutor | `padre1`, `padre2` | `padre2` tiene reportes; `padre1` tiene un vínculo pendiente (vincular con `PC-J00005`) |
| Profesional | `profesional1` | 2 clientes con próximas consultas |
| Admin | `admin` | Usuarios, videojuegos, plataformas, catálogo de logros y recursos |

## Estructura

```
src/app/
├─ components/   un componente por pantalla (lista-X, grabar-X, detalle-X) agrupados por módulo
├─ models/       interfaces con los campos exactos de los DTO + valores permitidos
├─ modules/material/   material-module.ts (todo Angular Material)
├─ services/     un servicio por recurso del backend (+ auth y notificaciones)
├─ interceptors/ JWT y cierre de sesión ante 401
└─ guards/       auth-guard y rol-guard
```

Diseño: azul `#0D0163`, verde `#45FD0D`, gris `#C9C9C9` (variables en `src/app/app.css`);
fuentes Meera Inimai y Poppins. Pensado para escritorio; bajo 768 px el menú pasa a hamburguesa.

## Qué está simulado

Marcado con `SIMULADO` en el código (el backend no lo ofrece):

- **Amigos**: lista de ejemplo local; las solicitudes no se envían.
- **Mensajes entre usuarios**: no existen.
- **Formulario de contacto de Soporte**: no envía nada.
- **Bloqueo real de juegos**: el método de bloqueo del límite solo se guarda, no bloquea nada.
