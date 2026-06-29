import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ActualizarClaseRequest, Clase, CrearClaseRequest } from '../../models/clase';

@Component({
  selector: 'app-clase-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './clase-form.html',
})
export class ClaseForm implements OnChanges{
  private readonly fb = inject(FormBuilder);

  @Input() claseSeleccionada: Clase | null = null;
  @Output() crearClase = new EventEmitter<CrearClaseRequest>();
  @Output() actualizarClase = new EventEmitter< {id: number; data: ActualizarClaseRequest}>();
  @Output() cancelarEdicion = new EventEmitter<void>();

  form = this.fb.nonNullable.group({
    nombre: ["", [Validators.required, Validators.minLength(3)]],
    descripcion: ["", [Validators.required, Validators.minLength(5)]],
    trainer: ["", [Validators.required, Validators.minLength(3)]],
    cupos: [1, [Validators.required, Validators.min(1)]]
  })

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["claseSeleccionada"] && this.claseSeleccionada) {
      this.form.patchValue({
        nombre: this.claseSeleccionada.nombre,
        descripcion: this.claseSeleccionada.descripcion,
        trainer: this.claseSeleccionada.trainer,
        cupos: Number(this.claseSeleccionada.cupos)
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsDirty();
      return
    }

    const data = this.form.getRawValue();

    if (this.claseSeleccionada) {
      this.actualizarClase.emit({
        id: this.claseSeleccionada.id,
        data
      });
    } else {
      this.crearClase.emit(data);
    }
    
    this.limpiarFormulario();
  }

  limpiarFormulario(): void {
    this.form.reset({
      nombre: "",
      descripcion: "",
      trainer: "",
      cupos: 1
    })
  }

  cancelar(): void {
    this.limpiarFormulario();
    this.cancelarEdicion.emit();
  }
}
