import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Usuario } from '../../../usuarios/models/usuario';
import { UsuarioService } from '../../../usuarios/services/usuario';
import { Rol } from '../../../roles/models/rol';
import { RolService } from '../../../roles/services/rol';
import { RolUsuarioService } from '../../services/rol-usuario';

@Component({
  selector: 'app-rol-usuario-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './rol-usuario-form.html'
})
export class RolUsuarioForm implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly usuarioService = inject(UsuarioService);
  private readonly rolService = inject(RolService);
  private readonly rolUsuarioService = inject(RolUsuarioService);
  private readonly cdr = inject(ChangeDetectorRef);

  usuarios: Usuario[] = [];
  roles: Rol[] = [];

  isLoading = false;
  successMessage = '';
  errorMessage = '';

  form = this.fb.nonNullable.group({
    idUsuario: [0, [Validators.required, Validators.min(1)]],
    idRol: [0, [Validators.required, Validators.min(1)]]
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.isLoading = true;

    this.usuarioService.listar().subscribe({
      next: (usuarios) => {
        this.usuarios = [...usuarios].sort((a, b) => a.id - b.id);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL CARGAR USUARIOS:', error);
        this.errorMessage = 'No se pudieron cargar los usuarios.';
        this.cdr.detectChanges();
      }
    });

    this.rolService.listar().subscribe({
      next: (roles) => {
        this.roles = [...roles].sort((a, b) => a.id - b.id);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL CARGAR ROLES:', error);
        this.errorMessage = 'No se pudieron cargar los roles.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    const data = {
      usuarioId: Number(
        this.form.controls.idUsuario.value
      ),
      rolId: Number(
        this.form.controls.idRol.value
      )
    };
    
    this.rolUsuarioService.asignar(data).subscribe({
      next: () => {
        this.successMessage = 'Rol asignado correctamente.';
        this.form.reset({
          idUsuario: 0,
          idRol: 0
        });
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('ERROR AL ASIGNAR ROL:', error);
        this.errorMessage = 'No se pudo asignar el rol.';
        this.cdr.detectChanges();
      }
    });
  }
}