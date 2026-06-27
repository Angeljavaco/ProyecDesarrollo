import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { CrearUsuarioComponent } from './components/crear-usuario/crear-usuario.component';
import { EliminarUsuarioComponent } from './components/eliminar-usuario/eliminar-usuario.component';
import { CrearClaseComponent } from './components/crear-clase/crear-clase.component';
import { ActualizarClaseComponent } from './components/actualizar-clase/actualizar-clase.component';
import { CrearReservaComponent } from './components/crear-reserva/crear-reserva.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'usuarios/crear', component: CrearUsuarioComponent },
  { path: 'usuarios/eliminar', component: EliminarUsuarioComponent },
  { path: 'clases/crear', component: CrearClaseComponent },
  { path: 'clases/actualizar', component: ActualizarClaseComponent },
  { path: 'reservas/crear', component: CrearReservaComponent },
  { path: '**', redirectTo: 'login' }
];
