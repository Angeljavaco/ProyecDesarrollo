export interface ReporteInscrito {
  usuarioId: number;
  nombre: string;
  email: string;
  telefono: string;
}

export interface ReporteClase {
  claseId: number;
  claseNombre: string;
  descripcion: string;
  fecha: string;
  estado: string;

  trainerId: number;
  trainerNombre: string;

  cuposTotales: number;
  totalInscritos: number;
  cuposDisponibles: number;

  inscritos: ReporteInscrito[];
}