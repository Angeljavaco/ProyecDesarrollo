import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { APP_CONFIG } from '../../../core/config/app-config';
import { ReporteClase } from '../models/reporte';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    `${APP_CONFIG.api.baseUrl}/reportes`;

  obtenerInscritosPorClase(
    claseId: number
  ): Observable<ReporteClase> {
    return this.http.get<ReporteClase>(
      `${this.apiUrl}/clases/${claseId}/inscritos`
    );
  }
}