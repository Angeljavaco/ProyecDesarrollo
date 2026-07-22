export interface RolUsuario {
  id: number;
  usuarioId: number;
  rolId: number;
}

export interface AsignarRolRequest {
  usuarioId: number;
  rolId: number;
}