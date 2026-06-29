import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";

import { APP_CONFIG } from "../../../core/config/app-config";
import { CrearUsuarioRequest, Usuario } from "../models/usuario";
import { Observable } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class UsuarioService {
    private readonly http = inject(HttpClient);
    private readonly apiUrl = `${APP_CONFIG.api.baseUrl}/usuarios`;

    listar(): Observable<Usuario[]> {
        return this.http.get<Usuario[]>(this.apiUrl);
    } 

    crear(usuario: CrearUsuarioRequest): Observable <Usuario> {
        return this.http.post<Usuario>(this.apiUrl, usuario);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}