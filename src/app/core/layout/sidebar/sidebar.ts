import { Component, inject } from '@angular/core';
import {
  RouterLink,
  RouterLinkActive
} from '@angular/router';

import { AuthService } from '../../../features/auth/services/auth';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './sidebar.html'
})
export class Sidebar {
  readonly authService = inject(AuthService);
}