import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';
import { Login } from './features/auth/pages/login/login';
import { DashboardLayout } from './core/layout/dashboard-layout/dashboard-layout';

export const routes: Routes = [
    {
        path: "login",
        component: Login
    },
    {
        path: "dashboard",
        canActivate: [authGuard],
        component: DashboardLayout,
        children: [
            {
                path:"",
                redirectTo: "usuarios",
                pathMatch: "full"
            },
            {
                path: "usuarios",
                loadComponent: () => 
                    import("./features/usuarios/pages/usuario-page/usuario-page")
                        .then(m => m.UsuarioPage)
            },
            {
                path: "clases",
                loadComponent: () =>
                    import("./features/clases/pages/clases-page/clases-page")
                    .then(m => m.ClasesPage)
            },
            {
                path: "reservas",
                loadComponent: () =>
                    import("./features/reservas/pages/reservas-page/reservas-page")
                    .then(m => m.ReservasPage)
            },
            {
                path: "rol-usuario",
                loadComponent: () =>
                    import("./features/rol-usuario/pages/rol-usuario-page/rol-usuario-page")
                    .then(m => m.RolUsuarioPage)
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
