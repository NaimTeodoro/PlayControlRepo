import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HistorialAlerta } from '../models/historial-alerta';

// /historial-alertas (JUGADOR, PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class HistorialAlertaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/historial-alertas`;

  porAlerta(idAlerta: number): Observable<HistorialAlerta[]> {
    return this.http.get<HistorialAlerta[]>(`${this.url}/alerta/${idAlerta}`);
  }

  porUsuario(idUsuario: number): Observable<HistorialAlerta[]> {
    return this.http.get<HistorialAlerta[]>(`${this.url}/usuario/${idUsuario}`);
  }
}
