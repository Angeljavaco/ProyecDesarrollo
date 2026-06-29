import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { APP_CONFIG } from '../../../core/config/app-config';
import { Rol } from '../models/rol';

@Injectable({
  providedIn: 'root'
})
export class RolService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${APP_CONFIG.api.baseUrl}/roles`;

  listar(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.apiUrl);
  }
}