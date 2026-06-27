import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Clase } from '../models/clase.model';

@Injectable({
  providedIn: 'root'
})
export class ClaseService {
  private readonly apiUrl = `${environment.apiUrl}/clases`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Clase[]> {
    return this.http.get<Clase[]>(this.apiUrl);
  }

  crear(clase: Clase): Observable<Clase> {
    return this.http.post<Clase>(this.apiUrl, clase);
  }

  actualizar(id: number, clase: Clase): Observable<Clase> {
    return this.http.put<Clase>(`${this.apiUrl}/${id}`, clase);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
