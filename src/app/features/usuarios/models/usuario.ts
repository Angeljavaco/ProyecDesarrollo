export interface Usuario {
    id: number;
    nombre: string;
    email: string;
    telefono: string;
}

export interface CrearUsuarioRequest {
    nombre: string; 
    email: string;
    password: string;
    telefono: string;
}