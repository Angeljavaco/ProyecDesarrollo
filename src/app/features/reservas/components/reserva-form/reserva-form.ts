import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CrearReservaRequest } from '../../models/reserva';

@Component({
  selector: 'app-reserva-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reserva-form.html',
})
export class ReservaForm {
  private readonly fb = inject(FormBuilder)

  @Output() crearReserva = new EventEmitter<CrearReservaRequest>();

  form = this.fb.nonNullable.group({
    idUsuario: [1, [Validators.required, Validators.min(1)]],
    idClase: [1, [Validators.required, Validators.min(1)]],
    fechaReserva: ['', [Validators.required]],
    estado: ['ACTIVA', [Validators.required]]
  });

  onSubmit(): void {
  if (this.form.invalid) {
    this.form.markAllAsTouched();
    return;
  }

  const data: CrearReservaRequest = {
    idUsuario: Number(this.form.controls.idUsuario.value),
    idClase: Number(this.form.controls.idClase.value),
    fechaReserva: this.form.controls.fechaReserva.value,
    estado: this.form.controls.estado.value
  };

  this.crearReserva.emit(data);

  this.form.reset({
    idUsuario: 1,
    idClase: 1,
    fechaReserva: '',
    estado: 'ACTIVA'
  });
}
}
