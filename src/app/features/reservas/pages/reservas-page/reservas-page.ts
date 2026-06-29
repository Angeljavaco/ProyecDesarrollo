import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReservaForm } from '../../components/reserva-form/reserva-form';
import { CrearReservaRequest, Reserva } from '../../models/reserva';
import { ReservaService } from '../../services/reserva';

@Component({
  selector: 'app-reservas-page',
  imports: [CommonModule, ReservaForm],
  templateUrl: './reservas-page.html'
})
export class ReservasPage implements OnInit {
  private readonly reservaService = inject(ReservaService);
  private readonly cdr = inject(ChangeDetectorRef);

  reservas: Reserva[] = [];
  isLoading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.reservaService.listar().subscribe({
      next: (reservas) => {
        this.reservas = [...reservas].sort((a, b) => a.id - b.id);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL CARGAR RESERVAS:', error);
        this.errorMessage = 'No se pudieron cargar las reservas.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  crearReserva(data: CrearReservaRequest): void {
    this.reservaService.crear(data).subscribe({
      next: () => {
        this.cargarReservas();
      },
      error: (error) => {
        console.error('ERROR AL CREAR RESERVA:', error);
        this.errorMessage = 'No se pudo crear la reserva.';
        this.cdr.detectChanges();
      }
    });
  }
}