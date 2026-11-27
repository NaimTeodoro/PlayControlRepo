// UsuarioRecursoEducativoDTO — /usuario-recursos (consumo de un recurso; 409 si el par ya existe → PUT)
export interface UsuarioRecursoEducativo {
  idUsuarioRecurso?: number;
  idUsuario: number;
  idRecurso: number;
  fechaAcceso?: string | null;
  tiempoVisualizacionMinutos?: number | null;
  completado?: boolean;
  valoracionUsuario?: number | null; // 1–5
  comentario?: string | null;
  tituloRecurso?: string; // solo lectura
}
