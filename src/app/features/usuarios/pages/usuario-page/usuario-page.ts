import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';



import { UsuarioService } from '../../services/usuario';
import { UsuarioForm } from '../../components/usuario-form/usuario-form';
import { CrearUsuarioRequest, Usuario } from '../../models/usuario';

@Component({
  selector: 'app-usuario-page',
  imports: [CommonModule, UsuarioForm],
  templateUrl: './usuario-page.html'
})
export class UsuarioPage implements OnInit {
  private readonly usuarioService = inject(UsuarioService);
  private readonly cdr = inject(ChangeDetectorRef);

  usuarios: Usuario[] = [];
  isLoading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.usuarioService.listar().subscribe({
      next: (usuarios) => {
        this.usuarios = [...usuarios];
        this.isLoading = false;

        this.cdr.detectChanges();
      },
      error: (error) => {
        this.errorMessage = 'No se pudieron cargar los usuarios.';
        this.isLoading = false;

        this.cdr.detectChanges();
      }
    });
  }

  crearUsuario(data: CrearUsuarioRequest): void {
    this.usuarioService.crear(data).subscribe({
      next: () => {
        this.cargarUsuarios();
      },
      error: (error) => {
        console.error('ERROR AL CREAR USUARIO:', error);
        this.errorMessage = 'No se pudo crear el usuario.';
        this.cdr.detectChanges();
      }
    });
  }

  eliminarUsuario(id: number): void {
    const confirmar = confirm("Seguro que deseas eliminar este usuario?");

    if (!confirmar) {
      return
    }

    this.usuarioService.eliminar(id).subscribe({
      next: () => {
        this.usuarios = this.usuarios.filter(usuario => usuario.id !== id);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error("Error al eliminar usuario:", error);
        this.errorMessage = "No se pudo eliminar el usuario";
        this.cdr.detectChanges();
      }
    })
  }
}