import { EstadoProfesional } from './profesional-salud';

// ProfesionalClienteDTO — /profesional-clientes (409 si el par profesional-jugador ya existe)
export interface ProfesionalCliente {
  idProfesionalCliente?: number;
  idProfesional: number;
  idJugadorJoven: number;
  fechaAsociacion?: string | null;
  estado?: EstadoProfesional;
  diagnostico?: string | null;
  planTratamiento?: string | null;
  frecuenciaConsulta?: string | null;
  proximaConsulta?: string | null;
  notas?: string | null;
  // Solo lectura: datos del jugador
  idUsuarioJugador?: number;
  nombreJugador?: string;
  apellidoJugador?: string;
}
