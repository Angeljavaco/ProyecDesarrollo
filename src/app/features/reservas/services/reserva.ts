import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { APP_CONFIG } from '../../../core/config/app-config';
import {
  CrearReservaRequest,
  Reserva
} from '../models/reserva';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl =
    `${APP_CONFIG.api.baseUrl}/reservas`;

  listarMisReservas(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(
      `${this.apiUrl}/mis-reservas`
    );
  }

  listarTodas(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(
      this.apiUrl
    );
  }

  crear(
    request: CrearReservaRequest
  ): Observable<Reserva> {
    return this.http.post<Reserva>(
      this.apiUrl,
      request
    );
  }

  cancelar(
    reservaId: number
  ): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${reservaId}`
    );
  }
}