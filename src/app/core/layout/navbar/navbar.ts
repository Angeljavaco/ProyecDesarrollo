import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../../features/auth/services/auth';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html'
})
export class Navbar {
  private readonly router = inject(Router);

  readonly authService = inject(AuthService);

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  get panelTitle(): string {
    if (this.authService.hasRole('ADMIN')) {
      return 'Panel de administración';
    }

    if (this.authService.hasRole('TRAINER')) {
      return 'Panel del entrenador';
    }

    if (this.authService.hasRole('MEMBER')) {
      return 'Panel del miembro';
    }

    return 'Panel del gimnasio';
  }

  get userRoleName(): string {
    if (this.authService.hasRole('ADMIN')) {
      return 'Administrador';
    }

    if (this.authService.hasRole('TRAINER')) {
      return 'Entrenador';
    }

    if (this.authService.hasRole('MEMBER')) {
      return 'Miembro';
    }

    return 'Usuario';
  }

  get userDescription(): string {
    if (this.authService.hasRole('ADMIN')) {
      return 'Gestión general';
    }

    if (this.authService.hasRole('TRAINER')) {
      return 'Gestión de clases';
    }

    if (this.authService.hasRole('MEMBER')) {
      return 'Reservas y clases';
    }

    return 'Gestión del gimnasio';
  }

  get userEmail(): string {
    return this.authService.getEmail() ?? '';
  }
}