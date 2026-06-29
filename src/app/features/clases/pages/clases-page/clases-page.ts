import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClaseForm } from '../../components/clase-form/clase-form';
import { ClaseService } from '../../services/clase';

import { ActualizarClaseRequest, Clase, CrearClaseRequest } from '../../models/clase';

@Component({
  selector: 'app-clases-page',
  imports: [CommonModule, ClaseForm],
  templateUrl: './clases-page.html',
})
export class ClasesPage implements OnInit{
  private readonly claseService = inject(ClaseService);
  private readonly cdr = inject(ChangeDetectorRef);

  clases: Clase[] = [];
  claseSeleccionada: Clase | null = null;
  isLoading = false;
  errorMessage = "";

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.isLoading = true;
    this.errorMessage = '';
  
    this.claseService.listar().subscribe({
      next: (clases) => {
        this.clases = [...clases].sort((a, b) => a.id - b.id);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error("ERROR AL CARGAR CLASES:", error);
        this.errorMessage = "No se pudieron cargar las clases...";
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  crearClase(data: CrearClaseRequest): void {
    this.claseService.crear(data).subscribe({
      next: () => {
        this.cargarClases();
      },
      error: (error) => {
        console.error("ERROR AL CREAR CLASE:", error);
        this.errorMessage = 'No se pudo crear la clase.';
        this.cdr.detectChanges();
      }
    })
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
