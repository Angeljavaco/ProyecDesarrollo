import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { APP_CONFIG } from '../../../core/config/app-config';
import { AsignarRolRequest, RolUsuario } from '../models/rol-usuario';

@Injectable({
  providedIn: 'root'
})
export class RolUsuarioService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${APP_CONFIG.api.baseUrl}/rol-usuario`;

  asignar(data: AsignarRolRequest): Observable<RolUsuario> {
    return this.http.post<RolUsuario>(this.apiUrl, data);
  }
}