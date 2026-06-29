import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { APP_CONFIG } from "../../../core/config/app-config";
import { ActualizarClaseRequest, Clase, CrearClaseRequest } from "../models/clase";

@Injectable({
    providedIn: "root"
})
export class ClaseService {
    private readonly http = inject(HttpClient);
    private readonly apiUrl = `${APP_CONFIG.api.baseUrl}/clases`;

    listar(): Observable<Clase[]> {
        return this.http.get<Clase[]>(this.apiUrl);
    }

    crear(clase: CrearClaseRequest): Observable<Clase> {
        return this.http.post<Clase>(this.apiUrl, clase);
    }

    actualizar(id: number, clase: ActualizarClaseRequest): Observable<Clase> {
        return this.http.put<Clase>(`${this.apiUrl}/${id}`, clase);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}