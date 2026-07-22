export interface Reserva {
  id: number;
  activo: boolean;
  usuarioId: number;
  usuarioNombre: string;
  claseId: number;
  claseNombre: string;
}

export interface CrearReservaRequest {
  claseId: number;
}