// Formato de error del backend (Spring ProblemDetail). El 400 de validación trae errores por campo.
export interface ProblemDetail {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  errores?: Record<string, string>;
}
