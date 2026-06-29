import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CrearUsuarioRequest } from '../../models/usuario';

@Component({
  selector: 'app-usuario-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './usuario-form.html'
})
export class UsuarioForm {
  private readonly fb = inject(FormBuilder);

  @Output() crearUsuario = new EventEmitter<CrearUsuarioRequest>();

  form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]],
    telefono: ['', [Validators.required, Validators.minLength(9)]]
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.crearUsuario.emit(this.form.getRawValue());
    this.form.reset();
  }
}