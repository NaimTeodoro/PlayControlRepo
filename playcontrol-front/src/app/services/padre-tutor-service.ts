import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PadreTutor } from '../models/padre-tutor';

// /padres-tutores (PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class PadreTutorService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/padres-tutores`;

  obtener(id: number): Observable<PadreTutor> {
    return this.http.get<PadreTutor>(`${this.url}/${id}`);
  }

  actualizar(id: number, padre: PadreTutor): Observable<PadreTutor> {
    return this.http.put<PadreTutor>(`${this.url}/${id}`, padre);
  }

  /** Perfil de padre de un usuario (un objeto; 404 si no existe) */
  porUsuario(idUsuario: number): Observable<PadreTutor> {
    return this.http.get<PadreTutor>(`${this.url}/usuario/${idUsuario}`);
  }
}
