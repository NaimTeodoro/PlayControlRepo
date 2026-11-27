// CitaDTO — /citas (solo lectura). Es la próxima consulta de una relación profesional–jugador ACTIVA.
// No trae diagnóstico, plan de tratamiento ni notas (datos clínicos solo del profesional).
export interface Cita {
  idProfesionalCliente: number;
  estado: string; // siempre "ACTIVO"
  frecuenciaConsulta?: string | null;
  proximaConsulta?: string | null; // yyyy-MM-ddTHH:mm:ss, puede ser null
  idJugadorJoven: number; // id de perfil
  idUsuarioJugador: number;
  nombreJugador?: string;
  apellidoJugador?: string;
  idProfesional: number;
  nombreProfesional?: string;
  apellidoProfesional?: string;
  especialidad?: string | null;
  institucion?: string | null;
  telefonoConsulta?: string | null;
  horarioConsulta?: string | null;
}
