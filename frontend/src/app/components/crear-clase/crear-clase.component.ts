import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClaseService } from '../../services/clase.service';

@Component({
  selector: 'app-crear-clase',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './crear-clase.component.html'
})
export class CrearClaseComponent {
  private readonly formBuilder = inject(FormBuilder);
  loading = false;
  successMessage = '';
  errorMessage = '';

  form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    descripcion: ['', [Validators.required, Validators.minLength(5)]],
    trainer: ['', [Validators.required, Validators.minLength(2)]],
    cupos: [1, [Validators.required, Validators.min(1)]]
  });

  constructor(
    private readonly claseService: ClaseService
  ) {}

  crearClase(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.claseService.crear(this.form.getRawValue()).subscribe({
      next: (clase) => {
        this.successMessage = `Clase creada correctamente${clase.id ? ` con ID ${clase.id}` : ''}.`;
        this.form.reset({ nombre: '', descripcion: '', trainer: '', cupos: 1 });
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo crear la clase. Verifica el token y el rol.';
        this.loading = false;
      }
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return Boolean(control?.invalid && (control.dirty || control.touched));
  }
}
