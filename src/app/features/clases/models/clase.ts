export type EstadoClase =
  | 'PROGRAMADA'
  | 'EN_CURSO'
  | 'FINALIZADA'
  | 'CANCELADA';

export interface Clase {
  id: number;
  nombre: string;
  descripcion: string;
  cupos: number;
  fecha: string;
  estado: EstadoClase;
  activo: boolean;
  trainerId: number;
  trainerNombre: string;
}

export interface CrearClaseRequest {
  nombre: string;
  descripcion: string;
  cupos: number;
  fecha: string;
}

export interface ActualizarClaseRequest {
  nombre: string;
  descripcion: string;
  cupos: number;
  fecha: string;
}