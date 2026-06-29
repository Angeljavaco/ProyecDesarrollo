import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";

import { APP_CONFIG } from "../../../core/config/app-config";
import { STORAGE_KEYS } from "../../../core/constants/storage-keys";
import { LoginRequest } from "../models/login-request";
import { LoginResponse } from "../models/login-response";

@Injectable({
    providedIn: "root"
})
export class AuthService {
    private readonly http = inject(HttpClient);
    private readonly apiUrl = `${APP_CONFIG.api.baseUrl}/auth`;

    login(credentials: LoginRequest): Observable<LoginResponse> {
        return this.http
            .post<LoginResponse>(`${this.apiUrl}/login`, credentials)
            .pipe(
                tap((response) => {
                    localStorage.setItem(STORAGE_KEYS.TOKEN, response.token);
                })
            );
    }

    logout(): void {
        localStorage.removeItem(STORAGE_KEYS.TOKEN);
    }

    getToken(): string | null {
        return localStorage.getItem(STORAGE_KEYS.TOKEN);
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }
}