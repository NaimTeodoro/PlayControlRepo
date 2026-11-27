// VideojuegoDTO — /videojuegos (GET cualquier autenticado; escritura solo ADMIN)
export interface Videojuego {
  idVideojuego?: number;
  idPlataforma: number;
  nombreJuego: string;
  genero?: string | null;
  desarrollador?: string | null;
  fechaLanzamiento?: string | null; // yyyy-MM-dd
  clasificacionPEGI?: string | null;
  descripcion?: string | null;
  tiempoPromedioSesion?: number | null; // minutos
  urlImagen?: string | null;
  activo?: boolean;
  fechaRegistro?: string | null; // solo lectura
}
