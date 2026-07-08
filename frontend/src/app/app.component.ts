import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Fitness App';

  constructor(private readonly authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }

  hasToken(): boolean {
    return Boolean(this.authService.getToken());
  }
}
