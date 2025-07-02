import { Component } from '@angular/core';
import { AuthService } from "../../../core/services/auth.service";
import { Router, RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export default class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        console.log('✅ ROLE:', this.authService.getRole());
        this.router.navigate(['/dashboard']);

      },
      error: err => {
        console.error('Login failed', err);
        alert('Login failed. Please check your credentials and try again.');
      }
    })
  }

}
