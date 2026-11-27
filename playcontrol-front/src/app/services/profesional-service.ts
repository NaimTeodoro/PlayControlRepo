import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProfesionalSalud } from '../models/profesional-salud';

// /profesionales-salud (PROFESIONAL y ADMIN)
@Injectable({ providedIn: 'root' })
export class ProfesionalService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/profesionales-salud`;

  obtener(id: number): Observable<ProfesionalSalud> {
    return this.http.get<ProfesionalSalud>(`${this.url}/${id}`);
  }

  actualizar(id: number, perfil: ProfesionalSalud): Observable<ProfesionalSalud> {
    return this.http.put<ProfesionalSalud>(`${this.url}/${id}`, perfil);
  }

  /** Perfil profesional de un usuario (un objeto; 404 si no existe) */
  porUsuario(idUsuario: number): Observable<ProfesionalSalud> {
    return this.http.get<ProfesionalSalud>(`${this.url}/usuario/${idUsuario}`);
  }
}
