import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router)

  isLoading = false;
  errorMessage = "";

  loginForm = this.fb.nonNullable.group({
    email: ["", [Validators.required, Validators.email]],
    password: ["", [Validators.required]]
  });

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsDirty();
      return;
    }

    this.isLoading = true;
    this.errorMessage = "";

    this.authService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.router.navigateByUrl("/dashboard");
      },
      error: () => {
        this.errorMessage = "Credenciales incorrectas o servidor no disponible";
        this.isLoading = false;
      }
    });
  }
}
