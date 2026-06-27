import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Clase } from '../../models/clase.model';
import { ClaseService } from '../../services/clase.service';

@Component({
  selector: 'app-actualizar-clase',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './actualizar-clase.component.html'
})
export class ActualizarClaseComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  clases: Clase[] = [];
  loading = false;
  loadingList = false;
  successMessage = '';
  errorMessage = '';

  form = this.formBuilder.nonNullable.group({
    id: [0, [Validators.required, Validators.min(1)]],
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    descripcion: ['', [Validators.required, Validators.minLength(5)]],
    trainer: ['', [Validators.required, Validators.minLength(2)]],
    cupos: [1, [Validators.required, Validators.min(1)]]
  });

  constructor(
    private readonly claseService: ClaseService
  ) {}

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.loadingList = true;
    this.claseService.listar().subscribe({
      next: (clases) => {
        this.clases = clases;
        this.loadingList = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar la lista de clases. Inicia sesion si la ruta esta protegida.';
        this.loadingList = false;
      }
    });
  }

  seleccionarClase(clase: Clase): void {
    this.form.patchValue({
      id: clase.id ?? 0,
      nombre: clase.nombre,
      descripcion: clase.descripcion,
      trainer: clase.trainer,
      cupos: clase.cupos
    });
  }

  actualizarClase(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { id, nombre, descripcion, trainer, cupos } = this.form.getRawValue();

    this.loading = true;
    this.claseService.actualizar(id, { nombre, descripcion, trainer, cupos }).subscribe({
      next: () => {
        this.successMessage = 'Clase actualizada correctamente.';
        this.loading = false;
        this.cargarClases();
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo actualizar la clase. Verifica el ID, token y rol.';
        this.loading = false;
      }
    });
  }

  eliminarClase(id: number | undefined): void {
    if (!id) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';
    this.loading = true;
    this.claseService.eliminar(id).subscribe({
      next: () => {
        this.successMessage = 'Clase eliminada correctamente.';
        this.loading = false;
        this.cargarClases();
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo eliminar la clase.';
        this.loading = false;
      }
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return Boolean(control?.invalid && (control.dirty || control.touched));
  }
}
