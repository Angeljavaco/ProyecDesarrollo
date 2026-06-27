import { Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../../models/usuario.model';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-eliminar-usuario',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './eliminar-usuario.component.html'
})
export class EliminarUsuarioComponent implements OnInit {
  private readonly formBuilder = inject(FormBuilder);
  usuarios: Usuario[] = [];
  loading = false;
  loadingList = false;
  successMessage = '';
  errorMessage = '';

  form = this.formBuilder.nonNullable.group({
    id: [0, [Validators.required, Validators.min(1)]]
  });

  constructor(
    private readonly usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.loadingList = true;
    this.usuarioService.listar().subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.loadingList = false;
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar la lista de usuarios. Inicia sesion si la ruta esta protegida.';
        this.loadingList = false;
      }
    });
  }

  seleccionarUsuario(id: number | undefined): void {
    if (id) {
      this.form.patchValue({ id });
    }
  }

  eliminarUsuario(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.usuarioService.eliminar(this.form.controls.id.value).subscribe({
      next: () => {
        this.successMessage = 'Usuario eliminado correctamente.';
        this.form.reset({ id: 0 });
        this.loading = false;
        this.cargarUsuarios();
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error?.message || 'No se pudo eliminar el usuario.';
        this.loading = false;
      }
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    return Boolean(control?.invalid && (control.dirty || control.touched));
  }
}
