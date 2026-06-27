import { Component, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-crear-usuario',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './crear-usuario.component.html'
})
export class CrearUsuarioComponent {
  private readonly formBuilder = inject(FormBuilder);
  loading = false;
  successMessage = '';
  errorMessage = '';

  form = this.formBuilder.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
    telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{7,15}$/)]]
  });

  constructor(
    private readonly usuarioService: UsuarioService
  ) {}

  crearUsuario(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.usuarioService.crear(this.form.getRawValue()).subscribe({
      next: (usuario) => {
        this.successMessage = `Usuario creado correctamente${usuario.id ? ` con ID ${usuario.id}` : ''}.`;
        this.form.reset();
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo crear el usuario.';
        this.loading = false;
      }
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return Boolean(control?.invalid && (control.dirty || control.touched));
  }
}
