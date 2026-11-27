// RecursoEducativoDTO — /recursos-educativos (DTO único; escritura PROFESIONAL y ADMIN)

export const TIPOS_RECURSO = [
  { valor: 'ARTICULO', etiqueta: 'Artículo', icono: 'article' },
  { valor: 'VIDEO', etiqueta: 'Video', icono: 'play_circle' },
  { valor: 'GUIA', etiqueta: 'Guía', icono: 'auto_stories' },
  { valor: 'FAQ', etiqueta: 'Pregunta frecuente', icono: 'help' },
] as const;
export type TipoRecurso = (typeof TIPOS_RECURSO)[number]['valor'];

export const AUDIENCIAS = [
  { valor: 'TODOS', etiqueta: 'Todos' },
  { valor: 'JUGADORES', etiqueta: 'Jugadores' },
  { valor: 'PADRES', etiqueta: 'Padres' },
  { valor: 'PROFESIONALES', etiqueta: 'Profesionales' },
] as const;
export type Audiencia = (typeof AUDIENCIAS)[number]['valor'];

export const NIVELES_DIFICULTAD = [
  { valor: 'BASICO', etiqueta: 'Básico' },
  { valor: 'INTERMEDIO', etiqueta: 'Intermedio' },
  { valor: 'AVANZADO', etiqueta: 'Avanzado' },
] as const;
export type NivelDificultad = (typeof NIVELES_DIFICULTAD)[number]['valor'];

export interface RecursoEducativo {
  idRecurso?: number;
  titulo: string;
  descripcion?: string | null;
  contenido?: string | null;
  tipoRecurso?: TipoRecurso | null;
  formato?: string | null;
  duracionMinutos?: number | null;
  audienciaObjetivo?: Audiencia | null;
  categoria?: string | null;
  subcategoria?: string | null;
  nivelDificultad?: NivelDificultad | null;
  urlRecurso?: string | null;
  urlImagen?: string | null;
  autor?: string | null;
  activo?: boolean;
  // Solo lectura (los gestiona el servidor)
  fechaPublicacion?: string | null;
  fechaActualizacion?: string | null;
  visitas?: number;
  valoracion?: number;
}
