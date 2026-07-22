import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClaseForm } from '../../components/clase-form/clase-form';
import { ClaseService } from '../../services/clase';

import { ActualizarClaseRequest, Clase, CrearClaseRequest } from '../../models/clase';
import { AuthService } from '../../../auth/services/auth';

@Component({
  selector: 'app-clases-page',
  imports: [CommonModule, ClaseForm],
  templateUrl: './clases-page.html',
})
export class ClasesPage implements OnInit{
  private readonly claseService = inject(ClaseService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly authService = inject(AuthService)

  clases: Clase[] = [];
  claseSeleccionada: Clase | null = null;
  isLoading = false;
  errorMessage = "";

  get totalCupos(): number {
  return this.clases.reduce((total, clase) => total + (clase.cupos as number), 0);
  }

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.isLoading = true;
    this.errorMessage = '';

    const request$ =
      this.authService.hasRole('ADMIN')
        ? this.claseService.listar()
        : this.claseService.listarMisClases();

    request$.subscribe({
      next: clases => {
        this.clases = clases;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: error => {
        console.error(
          'ERROR AL CARGAR CLASES:',
          error
        );

        this.errorMessage =
          'No se pudieron cargar las clases.';

        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  crearClase(data: CrearClaseRequest): void {
    this.errorMessage = '';

    this.claseService.crear(data).subscribe({
      next: () => {
        this.claseSeleccionada = null;
        this.cargarClases();
      },
      error: (error) => {
        console.error(
          'ERROR AL PROGRAMAR CLASE:',
          error
        );

        this.errorMessage =
          error?.error?.error ??
          'No se pudo programar la clase.';

        this.cdr.detectChanges();
      }
    });
  }

  seleccionarClase(clase: Clase): void {
    this.claseSeleccionada = { ...clase };
    this.cdr.detectChanges();
  }

  actualizarClase(event: { id: number; data: ActualizarClaseRequest}): void {
    this.claseService.actualizar(event.id, event.data).subscribe({
      next: (claseActualizada) => {
        this.clases = this.clases.map(clase =>
          clase.id === event.id ? claseActualizada : clase
        );

        this.claseSeleccionada = null;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL ACTUALIZAR CLASE:', error);
        this.errorMessage = 'No se pudo actualizar la clase.';
        this.cdr.detectChanges();
      }
    })
  }

  eliminarClase(id: number): void {
    const confirmar = confirm('¿Seguro que deseas eliminar esta clase?');

    if (!confirmar) {
      return;
    }

    this.claseService.eliminar(id).subscribe({
      next: () => {
        this.clases = this.clases.filter(clase => clase.id !== id);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL ELIMINAR CLASE:', error);
        this.errorMessage = 'No se pudo eliminar la clase.';
        this.cdr.detectChanges();
      }
    });
  }

  cancelarEdicion(): void {
    this.claseSeleccionada = null;
    this.cdr.detectChanges();
  } 
}
