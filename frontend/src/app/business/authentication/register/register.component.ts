import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../../../core/services/auth.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export default class RegisterComponent {

  fullName: string = '';
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {
  }

  register(): void {
    if (!this.fullName || !this.email || !this.password) {
      alert('Please fill in all fields.');
      return;
    }

    this.authService.register(this.fullName, this.email, this.password).subscribe({
      next: () => {
        alert('Registration successful!');
        this.router.navigate(['/login']);
      },
      error: err => {
        console.error('Registration failed:', err);
        alert('Registration failed. Please try again.');
      }
    });
  }
}
