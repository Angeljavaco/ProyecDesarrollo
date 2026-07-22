import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

import { Login } from './features/auth/pages/login/login';
import { DashboardLayout } from './core/layout/dashboard-layout/dashboard-layout';

export const routes: Routes = [
  {
    path: 'login',
    component: Login
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    component: DashboardLayout,
    children: [
      {
        path: 'usuarios',
        canActivate: [roleGuard],
        data: {
          roles: ['ADMIN']
        },
        loadComponent: () =>
          import(
            './features/usuarios/pages/usuario-page/usuario-page'
          ).then(module => module.UsuarioPage)
      },
      {
        path: 'clases',
        canActivate: [roleGuard],
        data: {
          roles: ['TRAINER', 'ADMIN']
        },
        loadComponent: () =>
          import(
            './features/clases/pages/clases-page/clases-page'
          ).then(module => module.ClasesPage)
      },
      {
        path: 'reservas',
        canActivate: [roleGuard],
        data: {
          roles: ['MEMBER', 'ADMIN']
        },
        loadComponent: () =>
          import(
            './features/reservas/pages/reservas-page/reservas-page'
          ).then(module => module.ReservasPage)
      },
      {
        path: 'reportes',
        canActivate: [roleGuard],
        data: {
          roles: ['TRAINER', 'ADMIN']
        },
        loadComponent: () =>
          import(
            './features/reportes/pages/reportes-page/reportes-page'
          ).then(module => module.ReportesPage)
      },
      {
        path: 'rol-usuario',
        canActivate: [roleGuard],
        data: {
          roles: ['ADMIN']
        },
        loadComponent: () =>
          import(
            './features/rol-usuario/pages/rol-usuario-page/rol-usuario-page'
          ).then(module => module.RolUsuarioPage)
      },
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'usuarios'
      }
    ]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];