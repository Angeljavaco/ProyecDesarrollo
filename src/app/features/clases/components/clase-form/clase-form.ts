import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject
} from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  ActualizarClaseRequest,
  Clase,
  CrearClaseRequest
} from '../../models/clase';

@Component({
  selector: 'app-clase-form',
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './clase-form.html'
})
export class ClaseForm implements OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() claseSeleccionada: Clase | null = null;

  @Output()
  crearClase = new EventEmitter<CrearClaseRequest>();

  @Output()
  actualizarClase = new EventEmitter<{
    id: number;
    data: ActualizarClaseRequest;
  }>();

  @Output()
  cancelarEdicion = new EventEmitter<void>();

  form = this.fb.nonNullable.group({
    nombre: [
      '',
      [
        Validators.required,
        Validators.minLength(3)
      ]
    ],
    descripcion: [
      '',
      [
        Validators.required,
        Validators.minLength(5)
      ]
    ],
    cupos: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ],
    fecha: [
      '',
      Validators.required
    ]
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (
      changes['claseSeleccionada'] &&
      this.claseSeleccionada
    ) {
      this.form.patchValue({
        nombre: this.claseSeleccionada.nombre,
        descripcion:
          this.claseSeleccionada.descripcion,
        cupos: Number(
          this.claseSeleccionada.cupos
        ),
        fecha: this.claseSeleccionada.fecha
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
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
  }

  limpiarFormulario(): void {
    this.form.reset({
      nombre: '',
      descripcion: '',
      cupos: 1,
      fecha: ''
    });
  }

  cancelar(): void {
    this.limpiarFormulario();
    this.cancelarEdicion.emit();
  }
}