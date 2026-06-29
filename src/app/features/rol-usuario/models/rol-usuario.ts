export interface RolUsuario {
  id: number;
  idUsuario: number;
  idRol: number;
}

export interface AsignarRolRequest {
  idUsuario: number;
  idRol: number;
}