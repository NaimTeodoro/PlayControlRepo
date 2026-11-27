// PlataformaDTO — /plataformas (GET cualquier autenticado; escritura solo ADMIN)

export const TIPOS_PLATAFORMA = [
  { valor: 'PC', etiqueta: 'PC', icono: 'computer' },
  { valor: 'CONSOLA', etiqueta: 'Consola', icono: 'sports_esports' },
  { valor: 'MOVIL', etiqueta: 'Móvil', icono: 'smartphone' },
] as const;
export type TipoPlataforma = (typeof TIPOS_PLATAFORMA)[number]['valor'];

export interface Plataforma {
  idPlataforma?: number;
  nombrePlataforma: string;
  tipo?: TipoPlataforma | null;
  descripcion?: string | null;
  desarrollador?: string | null;
  sistemaOperativo?: string | null;
  activa?: boolean;
  fechaCreacion?: string | null;
}
