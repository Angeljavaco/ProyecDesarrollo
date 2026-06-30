export interface Clase {
    id: number;
    nombre: string;
    descripcion: string;
    trainer: string;
    cupos: number;
}

export interface CrearClaseRequest {
    nombre: string; 
    descripcion: string;
    trainer: string;
    cupos: number;
}

export interface ActualizarClaseRequest {
    nombre: string;
    descripcion: string;
    trainer: string;
    cupos: number;
}