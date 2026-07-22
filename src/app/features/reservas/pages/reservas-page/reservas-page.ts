import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';
import { forkJoin } from 'rxjs';

import { AuthService } from '../../../auth/services/auth';
import { Clase } from '../../../clases/models/clase';
import { ClaseService } from '../../../clases/services/clase';

import { Reserva } from '../../models/reserva';
import { ReservaService } from '../../services/reserva';

@Component({
  selector: 'app-reservas-page',
  imports: [
    CommonModule
  ],
  templateUrl: './reservas-page.html'
})
export class ReservasPage implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly claseService = inject(ClaseService);
  private readonly reservaService =
    inject(ReservaService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  clasesDisponibles: Clase[] = [];
  misReservas: Reserva[] = [];

  isLoading = false;
  reservaEnProcesoId: number | null = null;
  cancelacionEnProcesoId: number | null = null;

  successMessage = '';
  errorMessage = '';

  get esAdmin(): boolean {
    return this.authService.hasRole('ADMIN');
  }

  get totalReservasActivas(): number {
    return this.misReservas.filter(
      reserva => reserva.activo
    ).length;
  }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;
    this.errorMessage = '';

    const reservasRequest = this.esAdmin
      ? this.reservaService.listarTodas()
      : this.reservaService.listarMisReservas();

    forkJoin({
      clases:
        this.claseService.listarDisponibles(),
      reservas: reservasRequest
    }).subscribe({
      next: ({ clases, reservas }) => {
        this.clasesDisponibles = clases;
        this.misReservas = reservas;

        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: error => {
        console.error(
          'ERROR AL CARGAR RESERVAS:',
          error
        );

        this.errorMessage =
          error?.error?.message ??
          error?.error?.error ??
          'No se pudo cargar la información.';

        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  reservarClase(claseId: number): void {
    this.reservaEnProcesoId = claseId;
    this.successMessage = '';
    this.errorMessage = '';

    this.reservaService
      .crear({ claseId })
      .subscribe({
        next: () => {
          this.successMessage =
            'Inscripción realizada correctamente.';

          this.reservaEnProcesoId = null;
          this.cargarDatos();
        },
        error: error => {
          console.error(
            'ERROR AL RESERVAR CLASE:',
            error
          );

          this.errorMessage =
            error?.error?.message ??
            error?.error?.error ??
            'No se pudo realizar la inscripción.';

          this.reservaEnProcesoId = null;
          this.cdr.detectChanges();
        }
      });
  }

  cancelarReserva(reservaId: number): void {
    const confirmed = window.confirm(
      '¿Deseas cancelar esta reserva?'
    );

    if (!confirmed) {
      return;
    }

    this.cancelacionEnProcesoId = reservaId;
    this.successMessage = '';
    this.errorMessage = '';

    this.reservaService
      .cancelar(reservaId)
      .subscribe({
        next: () => {
          this.successMessage =
            'Reserva cancelada correctamente.';

          this.cancelacionEnProcesoId = null;
          this.cargarDatos();
        },
        error: error => {
          console.error(
            'ERROR AL CANCELAR RESERVA:',
            error
          );

          this.errorMessage =
            error?.error?.message ??
            error?.error?.error ??
            'No se pudo cancelar la reserva.';

          this.cancelacionEnProcesoId = null;
          this.cdr.detectChanges();
        }
      });
  }

  estaReservada(claseId: number): boolean {
    return this.misReservas.some(
      reserva =>
        reserva.claseId === claseId &&
        reserva.activo
    );
  }
}