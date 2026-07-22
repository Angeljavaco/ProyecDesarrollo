import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { APP_CONFIG } from '../../../core/config/app-config';
import { STORAGE_KEYS } from '../../../core/constants/storage-keys';
import { LoginRequest } from '../models/login-request';
import { LoginResponse } from '../models/login-response';

interface JwtPayload {
  sub?: string;
  roles?: string[];
  authorities?: string[]; 
  exp?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl =
    `${APP_CONFIG.api.baseUrl}/auth`;

  login(
    credentials: LoginRequest
  ): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(
        `${this.apiUrl}/login`,
        credentials
      )
      .pipe(
        tap(response => {
          localStorage.setItem(
            STORAGE_KEYS.TOKEN,
            response.token
          );
        })
      );
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
  }

  getToken(): string | null {
    return localStorage.getItem(
      STORAGE_KEYS.TOKEN
    );
  }

  isAuthenticated(): boolean {
    const token = this.getToken();

    if (!token) {
      return false;
    }

    const payload = this.getPayload();

    if (!payload?.exp) {
      return true;
    }

    return payload.exp * 1000 > Date.now();
  }

  getRoles(): string[] {
    const payload = this.getPayload();

    if (!payload) {
      return [];
    }

    const roles =
      payload.roles ??
      payload.authorities ??
      [];

    return roles.map(role =>
      role.replace('ROLE_', '')
    );
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  hasAnyRole(allowedRoles: string[]): boolean {
    const currentRoles = this.getRoles();

    return allowedRoles.some(role =>
      currentRoles.includes(role)
    );
  }

  getEmail(): string | null {
    return this.getPayload()?.sub ?? null;
  }

  getDefaultDashboardRoute(): string {
    if (this.hasRole('ADMIN')) {
      return '/dashboard/usuarios';
    }

    if (this.hasRole('TRAINER')) {
      return '/dashboard/clases';
    }

    if (this.hasRole('MEMBER')) {
      return '/dashboard/reservas';
    }

    return '/login';
  }

  private getPayload(): JwtPayload | null {
    const token = this.getToken();

    if (!token) {
      return null;
    }

    try {
      const payloadBase64 = token.split('.')[1];

      if (!payloadBase64) {
        return null;
      }

      const normalizedBase64 = payloadBase64
        .replace(/-/g, '+')
        .replace(/_/g, '/');

      const decodedPayload = decodeURIComponent(
        atob(normalizedBase64)
          .split('')
          .map(character =>
            `%${(
              '00' +
              character.charCodeAt(0).toString(16)
            ).slice(-2)}`
          )
          .join('')
      );

      return JSON.parse(decodedPayload) as JwtPayload;
    } catch (error) {
      console.error(
        'No se pudo leer el JWT:',
        error
      );

      return null;
    }
  }
}