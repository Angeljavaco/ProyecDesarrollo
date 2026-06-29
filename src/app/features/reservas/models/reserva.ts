export interface Reserva {
    id: number;
    idUsuario: number;
    idClase: number;
    fechaReserva: string;
    estado: string;
}

export interface CrearReservaRequest {
    idUsuario: number;
    idClase: number;
    fechaReserva: string;
    estado: string;
}