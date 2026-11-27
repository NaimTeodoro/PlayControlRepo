// Roles que devuelve el backend en el login (Spring Security authorities)
export type Rol = 'ROLE_JUGADOR' | 'ROLE_PADRE' | 'ROLE_PROFESIONAL' | 'ROLE_ADMIN';

// Datos de presentación de cada rol (etiqueta, icono y ruta de inicio)
export const INFO_ROLES: Record<Rol, { etiqueta: string; icono: string; inicio: string }> = {
  ROLE_JUGADOR: { etiqueta: 'Jugador', icono: 'sports_esports', inicio: '/inicio-jugador' },
  ROLE_PADRE: { etiqueta: 'Padre / Tutor', icono: 'family_restroom', inicio: '/inicio-padre' },
  ROLE_PROFESIONAL: { etiqueta: 'Profesional', icono: 'psychology', inicio: '/inicio-profesional' },
  ROLE_ADMIN: { etiqueta: 'Administrador', icono: 'admin_panel_settings', inicio: '/inicio-admin' },
};
