import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Clase } from '../../models/clase.model';
import { Usuario } from '../../models/usuario.model';
import { ClaseService } from '../../services/clase.service';
import { ReservaService } from '../../services/reserva.service';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-crear-reserva',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './crear-reserva.component.html'
})
export class CrearReservaComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  usuarios: Usuario[] = [];
  clases: Clase[] = [];
  loading = false;
  loadingData = false;
  successMessage = '';
  errorMessage = '';

  form = this.formBuilder.nonNullable.group({
    idUsuario: [0, [Validators.required, Validators.min(1)]],
    idClase: [0, [Validators.required, Validators.min(1)]],
    fechaReserva: ['', [Validators.required]],
    estado: ['CONFIRMADA', [Validators.required, Validators.minLength(3)]]
  });

  constructor(
    private readonly reservaService: ReservaService,
    private readonly usuarioService: UsuarioService,
    private readonly claseService: ClaseService
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.loadingData = true;
    this.usuarioService.listar().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.cargarClases();
      },
      error: () => {
        this.errorMessage = 'No se pudieron cargar usuarios. Inicia sesion si la ruta esta protegida.';
        this.loadingData = false;
      }
    });
  }

  private cargarClases(): void {
    this.claseService.listar().subscribe({
      next: (clases) => {
        this.clases = clases;
        this.loadingData = false;
      },
      error: () => {
        this.errorMessage = 'No se pudieron cargar clases. Inicia sesion si la ruta esta protegida.';
        this.loadingData = false;
      }
    });
  }

  crearReserva(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.reservaService.crear(this.form.getRawValue()).subscribe({
      next: (reserva) => {
        this.successMessage = `Reserva creada correctamente${reserva.id ? ` con ID ${reserva.id}` : ''}.`;
        this.form.reset({ idUsuario: 0, idClase: 0, fechaReserva: '', estado: 'CONFIRMADA' });
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo crear la reserva. Verifica token y rol.';
        this.loading = false;
      }
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return Boolean(control?.invalid && (control.dirty || control.touched));
  }
}
